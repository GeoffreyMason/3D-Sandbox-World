package weapons;


public enum PaintColour {
	Green("Green", 0, 210, 0), Purple("Purple", 160, 16, 180), White("White", 255, 255, 255), Blue("Blue", 0, 0, 255), Red("Red", 255, 0, 0);

	String tileName;
	int red, green, blue;

	PaintColour(String tileName, int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.tileName = tileName;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

}
