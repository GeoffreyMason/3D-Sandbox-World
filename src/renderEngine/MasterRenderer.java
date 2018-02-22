package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.MovingEntity;
import models.RawModel;
import shaders.StaticShader;
import shadows.ShadowMapMasterRenderer;
import skybox.SkyboxRenderer;
import terrain.Chunk;
import terrain.TerrainShader;
import water.WaterFrameBuffers;
import water.WaterModel;
import water.WaterRenderer;
import water.WaterShader;

public class MasterRenderer {

	public static final float FOV = 50;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000;

	private static final float RED = 1;
	private static final float GREEN = 1;
	private static final float BLUE = 1;

	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();

	private WaterRenderer waterRenderer;
	private WaterShader waterShader = new WaterShader();
	
	private ShadowMapMasterRenderer shadowMapRenderer;

	private Map<RawModel, List<Entity>> entities = new HashMap<RawModel, List<Entity>>();
	private List<Chunk> terrains = new ArrayList<Chunk>();
	private List<WaterModel> waters = new ArrayList<WaterModel>();
	
	private SkyboxRenderer skyboxRenderer;

	public MasterRenderer(Loader loader, Camera camera, WaterFrameBuffers buffers) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		waterRenderer = new WaterRenderer(loader, waterShader, projectionMatrix, buffers);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void renderScene(List<Chunk> terrains, List<Entity> entities, List<Light> lights, Camera camera, Vector4f clipPlane) {
		for (Chunk terrain : terrains) {
			processTerrain(terrain);
		}
		for (Entity entity : entities) {
			processEntity(entity);
		}
		render(lights, camera, clipPlane);
	}

	private void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		waterShader.start();
		waterShader.loadSkyColour(RED, GREEN, BLUE);
		waterShader.loadViewMatrix(camera);
		waterRenderer.render(waters);
		waterShader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		waters.clear();
		terrains.clear();
		entities.clear();
	}
	
	public void loadHighlightPosition(float x, float z, float spread, Vector3f paintColour) {
		terrainShader.start();
		terrainShader.loadObjectPosition(x, z);
		terrainShader.loadSpread(spread);
		terrainShader.loadPaintColour(paintColour);
		terrainShader.stop();
	}

	public void processTerrain(Chunk terrain) {
		terrains.add(terrain);
	}
	
	public void processWater(WaterModel water) {
		waters.add(water);
	}

	public void processEntity(Entity entity) {
		RawModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void renderShadowMap(List<Entity> entityList, Light sun) {
		for (Entity entity: entityList) {
			processEntity(entity);
		}
		shadowMapRenderer.render(entities, sun);
		entities.clear();
	}
	
	public int getShadowMapTexture() {
		return shadowMapRenderer.getShadowMap();
	}

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
		waterShader.cleanUp();
		shadowMapRenderer.cleanUp();
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}

	private void createProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

}