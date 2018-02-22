package guis;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import fontRendering.TextMaster;
import gameState.MainGame;
import gameState.StateManager;
import gameState.StateManager.GameState;
import postProcessing.Fbo;
import renderEngine.DisplayManager;
import renderEngine.Loader;

public class GameMenuStartUp {

	private static Button buttonResume, buttonQuit;
	private static float startUpTimer, closeDownTimer;
	private List<Button> buttons = new ArrayList<>();
	
	private GuiTexture backgroundTexture;
	private GuiRenderer guiRenderer;
	private Loader loader;
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	private Fbo background;

	public GameMenuStartUp() {
		loader = new Loader();
		guiRenderer = new GuiRenderer(loader);
		buttonResume = new Button("Resume", 0, 0, 3);
		buttonQuit = new Button("Quit", 0, 0.15f, 3);
		buttons.add(buttonResume);
		buttons.add(buttonQuit);
		background = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		backgroundTexture = new GuiTexture(background.getColourTexture(), new Vector2f(0, 0), 180,
				new Vector2f(Display.getWidth(), Display.getHeight()), true);
		guis.add(backgroundTexture);
	}

	public boolean isStartUpComplete() {
		//startUpTimer += DisplayManager.getFrameTimeSeconds();
		buttonResume.isMovementComplete(1.2f, 0, 0, 0, 1, startUpTimer);
		buttonQuit.isMovementComplete(1.2f, 0.15f, 0, 0.15f, 1, startUpTimer);
		/*
		 * if (buttonResume.isMovementComplete(1.2f, 0, 0, 0, 1)) {
		 * //startUpTimer += Display return true; }
		 */
		return false;
	}
	
	public void update() {
		background = MainGame.getMenuBackground();
		guiRenderer.render(guis);
		TextMaster.render();
	}
	
	public void cleanUp() {
		guiRenderer.cleanUp();
		background.cleanUp();
		loader.cleanUp();
		background.cleanUp();
	}
}
