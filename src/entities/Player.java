package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.DisplayManager;
import terrain.Chunk;

public class Player extends Entity {

	private static final float RUN_SPEED = 50;
	private static final float TURN_SPEED = 160;
	public static final float GRAVITY = -80;
	private static final float JUMP_POWER = 32;

	private float currentSpeed = 0;
	private float currentLateralSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardSpeed = 0;
	private float timeInterval;

	public boolean creative = true;
	private boolean clockStarted;

	public Player(RawModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move(Chunk terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);

		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);

		float distanceLateral = currentLateralSpeed * DisplayManager.getFrameTimeSeconds();
		float dxL = (float) (distanceLateral * Math.sin(Math.toRadians(super.getRotY() - 90)));
		float dzL = (float) (distanceLateral * Math.cos(Math.toRadians(super.getRotY() - 90)));
		super.increasePosition(dxL, 0, dzL);
		if (!creative)
			upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			upwardSpeed = 0;
			super.getPosition().y = terrainHeight;
		}
	}

	private void jump() {
		this.upwardSpeed = JUMP_POWER;
	}

	public void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentLateralSpeed = -RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentLateralSpeed = RUN_SPEED;
		} else {
			this.currentLateralSpeed = 0;
		}
		this.currentTurnSpeed = -Mouse.getDX() * Camera.mouseSensitivity;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
			if (Mouse.isGrabbed())
				Mouse.setGrabbed(false);
			else
				Mouse.setGrabbed(true);
		}

		if (creative) {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				this.upwardSpeed = JUMP_POWER;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				this.upwardSpeed = -JUMP_POWER;
			} else
				this.upwardSpeed = 0;
		} else {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				jump();
			}
		}
	}

}
