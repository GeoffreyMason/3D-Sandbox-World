package models;

public class RawModel {
	
	private int vaoID;
	private int vboPosition;
	private int vboColour;
	private int vertexCount;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public RawModel(int vaoID, int vboPosition, int vertexCount) {
		this.vaoID = vaoID;
		this.vboPosition = vboPosition;
		this.vertexCount = vertexCount;
	}
	
	public RawModel(int vaoID, int vboPosition, int vboColour, int vertexCount) {
		this.vaoID = vaoID;
		this.vboPosition = vboPosition;
		this.vboColour = vboColour;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}
	
	public int getVboPosition() {
		return vboPosition;
	}
	
	public int getVboColour() {
		return vboColour;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

}
