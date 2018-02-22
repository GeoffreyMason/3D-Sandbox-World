package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {
	
	private static final int MAX_LIGHTS = 4;

	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationLightPosition[];
	private int locationLightColour[];
	private int locationAttenuation[];
	private int locationSkyColour;
	private int locationTime;
	private int locationPlane;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "RGBcolour");
	}

	@Override
	protected void getAllUniformLocations() {
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationSkyColour = super.getUniformLocation("skyColour");
		locationTime = super.getUniformLocation("time");
		locationPlane = super.getUniformLocation("plane");

		locationLightPosition = new int[MAX_LIGHTS];
		locationLightColour = new int[MAX_LIGHTS];
		locationAttenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			locationLightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}
	
	public void loadClipPlane(Vector4f plane) {
		super.load4DVector(locationPlane, plane);
	}
	
	public void loadTime(float time) {
		super.loadFloat(locationTime, time);
	}
	
	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(locationSkyColour, new Vector3f(r, g, b));
	}
	
	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
				super.loadVector(locationLightColour[i], lights.get(i).getColour());
				super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(locationLightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(locationLightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(locationAttenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(locationViewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(locationProjectionMatrix, projection);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
}
