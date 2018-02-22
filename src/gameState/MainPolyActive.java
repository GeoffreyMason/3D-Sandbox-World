package gameState;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import gameState.StateManager.GameState;
import renderEngine.DisplayManager;
import renderEngine.Loader;

public class MainPolyActive {

	public static final int seed = (int) (Math.random() * 10000000);

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		while (!Display.isCloseRequested() && !GameMenu.quitGame) {
			StateManager.update();
			DisplayManager.updateDisplay();
		}
		StateManager.cleanUp();
		DisplayManager.closeDisplay();
	}
}