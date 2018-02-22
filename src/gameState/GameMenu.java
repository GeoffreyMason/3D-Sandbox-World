package gameState;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import fontRendering.TextMaster;
import gameState.StateManager.GameState;
import guis.Button;
import guis.GuiRenderer;
import guis.GuiTexture;
import postProcessing.Fbo;
import renderEngine.DisplayManager;
import renderEngine.Loader;

public class GameMenu {

	private GuiTexture logoBorder, logo, backgroundTexture;
	private Loader loader = new Loader();
	private GuiRenderer guiRenderer;
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();

	public static boolean quitGame;

	private static float time;

	// private GuiManager guis;
	private static Fbo background;

	// private static ButtonSystem buttons;
	private List<Button> buttons = new ArrayList<Button>();
	private Button buttonResume, buttonQuit;

	public GameMenu() {
		setupGuis();
		guiRenderer = new GuiRenderer(loader);
		background = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		buttonResume = new Button("Resume", 0, 0, 3);
		buttonQuit = new Button("Quit", 0, 0.15f, 3);
		buttons.add(buttonResume);
		buttons.add(buttonQuit);
	}

	public static void startUp() {

	}

	public void setupGuis() {
		logo = new GuiTexture(loader.loadTexture("GeoffLogo"), new Vector2f(0, 0),
				new Vector2f(256 / 1.5f, 128 / 1.5f));
		logoBorder = new GuiTexture(loader.loadTexture("logoBorder2"), new Vector2f(0, 0),
				new Vector2f(256 / 1.5f, 256 / 1.5f));
		backgroundTexture = new GuiTexture(background.getColourTexture(), new Vector2f(0, 0), 180,
				new Vector2f(Display.getWidth(), Display.getHeight()), true);
		guis.add(backgroundTexture);
		guis.add(logo);
		guis.add(logoBorder);
	}

	private Vector2f interpolate(Vector2f initialPosition, Vector2f targetPosition, float blend) {
		double theta = blend * Math.PI;
		float f = (float) ((1f - Math.cos(theta)) * 0.5f);

		return new Vector2f(initialPosition.x * (1f - f) + targetPosition.x * f,
				initialPosition.y * (1f - f) + targetPosition.y * f);
	}

	private void updateGuis() {
		// buttons.move(buttonResume, 0.6f, 0.3f, 5);

		// buttonResume.move(0.0f, 0.0f, 1f);

		float targetTime = 1f;
		Vector2f initialPosition = new Vector2f(1.2f, 0.4f);
		Vector2f targetPosition = new Vector2f(0f, 0.4f);
		if (time < targetTime) {
			time += DisplayManager.getFrameTimeSeconds();
			Vector2f currentPos = interpolate(initialPosition, targetPosition, time / targetTime);
			logo.setPosition(currentPos);
			logoBorder.setPosition(currentPos);
		}
		logoBorder.setRotZ(logoBorder.getRotZ() - 34f * DisplayManager.getFrameTimeSeconds());
	}

	public static void updateBackground() {
		time = 0;
		background = MainGame.getMenuBackground();
	}

	public void cleanUp() {
		guiRenderer.cleanUp();
		loader.cleanUp();
		background.cleanUp();
	}

	public void update() {
		updateGuis();
		for (Button button : buttons) {
			button.isPressed();
		}
		if (buttonResume.isPressed()) {
			buttonResume.getText().setPosition(new Vector2f(-2, 0));
			buttonQuit.getText().setPosition(new Vector2f(-2, 0));
			StateManager.setGameState(GameState.MAINGAME);
		}
		if (buttonQuit.isPressed()) {
			GameMenu.quitGame = true;
		}
		guiRenderer.render(guis);

		TextMaster.render();
	}
}