package guis;

import java.io.File;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import renderEngine.DisplayManager;
import renderEngine.Loader;

public class Button {

	private float x, y, width, height, textPosX, textPosY, originalX, originalY;
	private int fontSize;
	private Loader loader = new Loader();
	private GUIText text;
	private FontType font = new FontType(loader.loadFontTexture("candara"), new File("res/candara.fnt"));

	public Button(String text, float x, float y, int fontSize) {
		this.originalX = x;
		this.originalY = y;
		this.x = (x + 1) / 2f * Display.getWidth();
		this.y = (y + 1) / 2f * Display.getHeight();
		this.textPosX = x / 2f;
		this.textPosY = (y + 1f) / 2f;
		this.text = new GUIText(text, fontSize, font, new Vector2f(textPosX, textPosY), 1, true);
		this.text.setColour(0.3f, 0.3f, 0.3f);
		this.width = this.text.getWidth() * Display.getWidth();
		this.height = 78 / 6f * fontSize;
		this.fontSize = fontSize;
	}

	public boolean isPressed() {
		float MouseX = Mouse.getX();
		float MouseY = Display.getHeight() - Mouse.getY();
		
		if (MouseX > x - width / 2f && MouseX < x + width / 2f && MouseY > y && MouseY < y + height) {
			// System.out.println("hit");
			text.setFontSize(fontSize * 1.1f);
			float deltaHeight = ((78 / 6f * fontSize * 1.1f - height) / 2f);
			text.setPosition(new Vector2f(textPosX, textPosY - deltaHeight / Display.getHeight()));
			if (Mouse.isButtonDown(0))
				return true;
		} else if (text.getFontSize() != fontSize) {
			text.setFontSize(fontSize);
			text.setPosition(new Vector2f(textPosX, textPosY));
		}
		return false;
	}
	
	public boolean isMovementComplete(float initialX, float initialY, float targetX, float targetY, float movementDuration, float time) {
		Vector2f initialPosition = new Vector2f(initialX, (initialY + 1f) / 2f);
		Vector2f targetPosition = new Vector2f(targetX, (targetY + 1f) / 2f);
		if (time < movementDuration) {
			time += DisplayManager.getFrameTimeSeconds();
			Vector2f currentPos = interpolatePosition(initialPosition, targetPosition, time / movementDuration);
			text.setPosition(currentPos);
			return false;
		} else {
			return true;
		}
	}

	private Vector2f interpolatePosition(Vector2f initialPosition, Vector2f targetPosition, float blend) {
		double theta = blend * Math.PI;
		float f = (float) ((1f - Math.cos(theta)) * 0.5f);
		return new Vector2f(initialPosition.x * (1f - f) + targetPosition.x * f,
				initialPosition.y * (1f - f) + targetPosition.y * f);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public GUIText getText() {
		return text;
	}

	public FontType getFont() {
		return font;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
