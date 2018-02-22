package particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/particles/particleVShader.txt";
	private static final String FRAGMENT_FILE = "src/particles/particleFShader.txt";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_modelViewMatrix;
	private int location_projectionMatrix;
	private int locationPlane;
	
	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelMatrix = super.getUniformLocation("modelMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		locationPlane = super.getUniformLocation("plane");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(3, "colour");
	}
	
	public void loadClipPlane(Vector4f plane) {
		super.load4DVector(locationPlane, plane);
	}

	protected void loadModelViewMatrix(Matrix4f model, Matrix4f view, Matrix4f modelView) {
		super.loadMatrix(location_modelViewMatrix, modelView);
		super.loadMatrix(location_modelMatrix, model);
		super.loadMatrix(location_viewMatrix, view);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
