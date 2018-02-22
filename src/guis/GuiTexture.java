package guis;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;

public class GuiTexture {

	private int texture;
	private Vector2f position;
	private Vector2f scale;
	private float rotX, rotZ;
	private boolean mirror = false;
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = new Vector2f(scale.x / Display.getWidth(), scale.y / Display.getWidth());
		this.rotX = 0;
		this.rotZ = 0;
	}
	
	public GuiTexture(int texture, Vector2f position, float rotZ, Vector2f scale, boolean mirror) {
		this.texture = texture;
		this.position = position;
		this.scale = new Vector2f(scale.x / Display.getWidth(), scale.y / Display.getWidth());
		this.rotX = 0;
		this.rotZ = rotZ;
		this.mirror = mirror;
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}
	
	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public boolean isMirror() {
		return mirror;
	}

	public void setScale(Vector2f scale) {
		this.scale.x = scale.x / Display.getWidth();
		this.scale.y = scale.y / Display.getWidth();
	}
	
}
