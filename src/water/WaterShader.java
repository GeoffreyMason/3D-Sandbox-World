package water;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import shaders.ShaderProgram;
import toolbox.Maths;

public class WaterShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/water/waterVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/water/waterFragmentShader.txt";
	private static final String GEOMETRY_FILE = "src/water/waterGeometryShader.txt";

	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationSkyColour;
	private int locationTime;
	private int locationReflectionTexture;
	private int locationRefractionTexture;
	private int locationCameraPosition;
	private int locationDudvMap;
	
	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, GEOMETRY_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationSkyColour = super.getUniformLocation("skyColour");
		locationTime = super.getUniformLocation("time");
		locationReflectionTexture = super.getUniformLocation("reflectionTexture");
		locationRefractionTexture = super.getUniformLocation("refractionTexture");
		locationCameraPosition = super.getUniformLocation("cameraPosition");
		locationDudvMap = super.getUniformLocation("dudvMap");
	}
	
	public void connectTextureUnits() {
		super.loadInt(locationReflectionTexture, 0);
		super.loadInt(locationRefractionTexture, 1);
		super.loadInt(locationDudvMap, 2);
	}
	
	public void loadTime(float time) {
		super.loadFloat(locationTime, time);
	}
	
	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(locationSkyColour, new Vector3f(r, g, b));
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(locationViewMatrix, viewMatrix);
		super.loadVector(locationCameraPosition, camera.getPosition());
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(locationProjectionMatrix, projection);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
}