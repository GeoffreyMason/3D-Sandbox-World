package terrain;


public enum TerrainType {
	Grass("Grass", true, 16, 160, 36), LightGrass("Grass", true, 16, 250, 36), PaleGrass("Grass", true, 150, 250, 180), Dirt("Dirt", true, 155, 118, 63), Snow("Snow", true, 255, 255, 255),
	Stone("Stone", true, 100, 100, 100), Purple("Stone", true, 200, 50, 200), Sand("Sand", true, 239, 221, 111);

	String tileName;
	int red, green, blue;
	boolean buildable;

	TerrainType(String tileName, boolean buildable, int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.buildable = buildable;
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
