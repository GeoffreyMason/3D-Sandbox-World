package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFromPlayer = -5;
	private float angleAroundPlayer = 0;
	private float cameraYOffset = 4;

	private Vector3f position = new Vector3f(5, 0, 5);
	private float pitch = 25;
	private float lastPitch;
	private float lastAngleAroundPlayer;
	private float yaw = 0;
	private float roll;
	public static float mouseSensitivity = 8;
	
	private Player player;

	public Camera(Player player) {
		this.player = player;
	}

	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.y = player.getPosition().y + verticalDistance;
		position.z = player.getPosition().z - offsetZ;
	}

	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch))) + cameraYOffset;
	}
	
	private void calculateZoom() {
		// float zoomLevel = Mouse.getDWheel() * 0.1f;
		// distanceFromPlayer -= zoomLevel;
		if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT))
			distanceFromPlayer += 0.2;
		if (Keyboard.isKeyDown(Keyboard.KEY_ADD))
			distanceFromPlayer -= 0.2;
	}

	private void calculatePitch() {
			float pitchChange = Mouse.getDY() * (mouseSensitivity / 50f);
			pitch -= pitchChange;
	}

	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}

	public void invertPitch() {
		this.pitch = -pitch;
	}

	public void setPitch(float pitch) {
		lastPitch = this.pitch;
		this.pitch = pitch;
	}

	public void resetPitch() {
		this.pitch = lastPitch;
	}

	public void setAngleAroundPlayer(float angleAroundPlayer) {
		this.angleAroundPlayer = angleAroundPlayer;
	}
	
	public void resetAngleAroundPlayer() {
		this.angleAroundPlayer = lastAngleAroundPlayer;
	}

}
