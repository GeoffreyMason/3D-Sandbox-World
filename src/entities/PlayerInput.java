package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import gameState.StateManager;
import gameState.StateManager.GameState;
import weapons.PaintColour;

public class PlayerInput {

	private int spread = 10, colourID, weaponID = 1, numberOfColours = 5;
	private float miningSpeed = 0.005f;
	private PaintColour colour;
	

	public PlayerInput() {

	}

	public void update() {
		keyboardInput();
		changeColour();
	}

	private void keyboardInput() {
		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
				weaponID = 1;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
				weaponID = 2;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				if (spread < 160)
				spread += 1;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				if (spread > 4)
				spread -= 1;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				if (weaponID == 2) {
					colourID += 1;
					if (colourID > numberOfColours - 1) {
						colourID = 0;
					}
				}
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				if (weaponID == 2) {
					colourID -= 1;
					if (colourID < 0) {
						colourID = numberOfColours - 1;
					}
				}
			}
		}
	}

	private void changeColour() {
		if (colourID == 0) {
			colour = PaintColour.Green;
		} else if (colourID == 1) {
			colour = PaintColour.Purple;
		} else if (colourID == 2) {
			colour = PaintColour.White;
		} else if (colourID == 3) {
			colour = PaintColour.Blue;
		} else if (colourID == 4) {
			colour = PaintColour.Red;
		}
	}

	public int getSpread() {
		return spread;
	}

	public void setSpread(int spread) {
		this.spread = spread;
	}

	public int getColourID() {
		return colourID;
	}

	public void setColourID(int colourID) {
		this.colourID = colourID;
	}

	public int getWeaponID() {
		return weaponID;
	}

	public void setWeaponID(int weaponID) {
		this.weaponID = weaponID;
	}

	public PaintColour getColour() {
		return colour;
	}

	public void setColour(PaintColour colour) {
		this.colour = colour;
	}

	public float getMiningSpeed() {
		return miningSpeed;
	}

	public void setMiningSpeed(float miningSpeed) {
		this.miningSpeed = miningSpeed;
	}

}
