package guis;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/guis/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/guis/guiFragmentShader.txt";

	private int location_transformationMatrix;
	private int location_flip;

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadFlip(int isFlipped) {
		super.loadInt(location_flip, isFlipped);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_flip = super.getUniformLocation("flip");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
