package gameState;

import guis.GameMenuStartUp;
import renderEngine.Loader;

public class StateManager {

	public static enum GameState {
		MAINGAME, GAMEMENU, GAMEMENUSTARTUP
	}

	public static GameState gameState = GameState.MAINGAME;

	public static MainGame mainGame;
	public static GameMenu gameMenu;
	public static GameMenuStartUp gameMenuStartUp;

	public static void update() {
		switch (gameState) {
		case MAINGAME:
			if (mainGame == null)
				mainGame = new MainGame();
			mainGame.update();
			break;
		case GAMEMENU:
			if (gameMenu == null)
				gameMenu = new GameMenu();
			gameMenu.update();
			break;
		case GAMEMENUSTARTUP:
			if (gameMenuStartUp == null)
				gameMenuStartUp = new GameMenuStartUp();
			gameMenuStartUp.update();
			break;
		}
	}

	public static void cleanUp() {
		mainGame.cleanUp();
		gameMenu.cleanUp();
	}

	public static GameState getGameState() {
		return gameState;
	}

	public static void setGameState(GameState gameState) {
		StateManager.gameState = gameState;
	}
}
