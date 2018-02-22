package gameState;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.MovingEntity;
import entities.Player;
import entities.PlayerInput;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import gameState.StateManager.GameState;
import guis.Button;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import particles.FireParticleSystem;
import particles.JetPackParticleSystem;
import particles.LaserParticleSystem;
import particles.ParticleMaster;
import particles.SmokeParticleSystem;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.ChunkSystem;
import terrain.Chunk;
import texturedParticles.ParticleTexture;
import texturedParticles.TexturedParticleMaster;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterModel;
import weapons.WeaponManager;
														
public class MainGame {

	private Loader loader = new Loader();
	private PlayerInput playerInput;
	private WeaponManager weaponManager;
	private FontType font;
	private Player player;
	private ChunkSystem chunkSystem;
	private Chunk chunk;
	private MasterRenderer renderer;
	private Camera camera;
	private WaterFrameBuffers waterFbos = new WaterFrameBuffers();

	private float[] colour = { 1, 1, 1 };
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Light> lights = new ArrayList<Light>();
	private List<Chunk> chunks = new ArrayList<Chunk>();;

	private Light sun = new Light(new Vector3f(200000, 2000000, 1000000), new Vector3f(1, 1, 1));

	private Fbo multisampleFbo, outputFbo, mapFbo;

	private WaterModel water;

	private MousePicker picker;
	private MousePicker screenCentre;

	private LaserParticleSystem laser;

	private static Fbo menuBackground;

	private GuiRenderer guiRenderer;
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	private GuiTexture logo, logoBorder;

	public static float time = 10;

	public MainGame() {
		TextMaster.init(loader);
		// font = new FontType(loader.loadFontTexture("candara"), new
		// File("res/candara.fnt"));
		// GUIText text = new GUIText("Geoff", 3, font, new Vector2f(0.0f,
		// 0.0f), 1f, true);
		setup();
		setupGuis();
	}

	public void cleanUp() {
		PostProcessing.cleanUp();
		outputFbo.cleanUp();
		multisampleFbo.cleanUp();
		waterFbos.cleanUp();
		mapFbo.cleanUp();
		ParticleMaster.cleanUp();
		TexturedParticleMaster.cleanUp();
		TextMaster.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		menuBackground.cleanUp();
	}

	private void setup() {

		menuBackground = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		Mouse.setGrabbed(true);
		guiRenderer = new GuiRenderer(loader);
		playerInput = new PlayerInput();
		weaponManager = new WeaponManager();

		ModelData sheepData = OBJFileLoader.loadOBJ("sheep");

		RawModel sheepModel = loader.loadToVAO(sheepData.getVertices(), sheepData.getNormals(), sheepData.getIndices(),
				colour);

		player = new Player(sheepModel, new Vector3f(200, 150, -150), 0, 0, 0, 1f);
		// chunkSystem = new ChunkSystem(loader, player);
		chunk = new Chunk(0, 0, loader);
		chunks.add(chunk);
		lights.add(sun);
		multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		mapFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
		PostProcessing.init(loader);

		camera = new Camera(player);
		renderer = new MasterRenderer(loader, camera, waterFbos);
		water = new WaterModel(0, 0, loader);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		TexturedParticleMaster.init(loader, renderer.getProjectionMatrix());
		entities.add(player);

		// terrains = chunkSystem.getChunkList();
		// currentChunk = chunk;

		picker = new MousePicker(camera, renderer.getProjectionMatrix(), chunk);
		screenCentre = new MousePicker(camera, renderer.getProjectionMatrix(), chunk,
				new Vector2f(Display.getWidth() / 2f, Display.getHeight() / 2f));
		laser = new LaserParticleSystem(new Vector3f(0, -1, 1), 100, 1000, 0, 1, 2);
		// GUIText text = new GUIText("Geoff", 3, font, new Vector2f(-0.45f,
		// 0.0f), 1f, true);
	}

	private void setupGuis() {
		// button = new Button(loader.loadTexture("GeoffLogo"), -0.5f, 0, 256,
		// 128);

		// GUIText text = new GUIText("Pause", 3, font, new
		// Vector2f(-0.45f,0.0f), 1, true);
		// button.getText();
		logo = new GuiTexture(loader.loadTexture("GeoffLogo"), new Vector2f(0, 1.3f),
				new Vector2f(256 / 1.5f, 128 / 1.5f));
		logoBorder = new GuiTexture(loader.loadTexture("logoBorder2"), new Vector2f(0, 1.3f),
				new Vector2f(256 / 1.5f, 256 / 1.5f));
		guis.add(logo);
		guis.add(logoBorder);
	}

	private Vector2f interpolate(Vector2f initialPosition, Vector2f targetPosition, float blend) {
		double theta = blend * Math.PI;
		float f = (float) ((1f - Math.cos(theta)) * 0.5f);

		return new Vector2f(initialPosition.x * (1f - f) + targetPosition.x * f,
				initialPosition.y * (1f - f) + targetPosition.y * f);
	}

	private void updateGuis() {

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			menuBackground.bindFrameBuffer();
			renderer.processWater(water);
			renderer.renderScene(chunks, entities, lights, camera, new Vector4f(0, 0, 0, 0));
			menuBackground.unbindFrameBuffer();
			PostProcessing.blur(menuBackground.getColourTexture());
			GameMenu.updateBackground();
			Mouse.setGrabbed(false);
			StateManager.setGameState(GameState.GAMEMENU);
		}
		float targetTime = 1f;
		Vector2f initialPosition = new Vector2f(0f, 0.4f);
		Vector2f targetPosition = new Vector2f(1.2f, 0.4f);
		if (time < targetTime) {
			time += DisplayManager.getFrameTimeSeconds();
			Vector2f currentPos = interpolate(initialPosition, targetPosition, time / targetTime);
			logo.setPosition(currentPos);
			logoBorder.setPosition(currentPos);
		}
		logoBorder.setRotZ(logoBorder.getRotZ() - 34f * DisplayManager.getFrameTimeSeconds());
	}

	public void update() {
		updateGuis();
		// chunkSystem.update();
		playerInput.update();
		picker.update();
		screenCentre.update();
		ParticleMaster.update(chunk);
		TexturedParticleMaster.update();
		renderer.renderShadowMap(entities, sun);
		camera.move();

		// currentChunk = chunkSystem.getCurrentChunk();
		float offsetScale = 10;
		float offsetDepth = 20;
		Vector3f direction = screenCentre.getCurrentRay();
		Vector3f perpendicular = Vector3f.cross(direction, new Vector3f(0, 1, 0), null);
		float yOffset = 5;
		Vector3f beamPosition = new Vector3f(
				player.getPosition().x + perpendicular.x * offsetScale + direction.x * offsetDepth,
				player.getPosition().y + perpendicular.y * offsetScale + direction.y * offsetDepth + yOffset,
				player.getPosition().z + perpendicular.z * offsetScale + direction.z * offsetDepth);
		Vector3f terrainPoint = screenCentre.getCurrentTerrainPoint();

		if (terrainPoint != null) {
			Vector3f laserDirection = new Vector3f(-beamPosition.x + terrainPoint.x, -beamPosition.y + terrainPoint.y,
					-beamPosition.z + terrainPoint.z);
			laser.setDirection(laserDirection);
			// paintBeam.setDirection(laserDirection);
		}

		Vector3f centrePoint = screenCentre.getCurrentTerrainPoint();

		if (centrePoint != null) {
			if (playerInput.getWeaponID() == 2)
				renderer.loadHighlightPosition(centrePoint.x, centrePoint.z, playerInput.getSpread(),
						new Vector3f(playerInput.getColour().getRed() / 255f, playerInput.getColour().getGreen() / 255f,
								playerInput.getColour().getBlue() / 255f));
			else
				renderer.loadHighlightPosition(centrePoint.x, centrePoint.z, playerInput.getSpread(),
						new Vector3f(1, 1, 1));
		}

		if (Mouse.isButtonDown(1)) {
			if (terrainPoint != null) {
				if (centrePoint != null) {
					Vector3f laserDirection = new Vector3f(-beamPosition.x + terrainPoint.x,
							-beamPosition.y + terrainPoint.y, -beamPosition.z + terrainPoint.z);
					weaponManager.update(loader, chunk, centrePoint, beamPosition, laserDirection, playerInput);
				}
			}
		}

		player.move(chunk);

		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

		mapFbo.bindFrameBuffer();
		camera.setPitch(90);
		float cameraHeight = camera.getPosition().y;
		camera.getPosition().y = 50;
		renderer.processWater(water);
		renderer.renderScene(chunks, entities, lights, camera, new Vector4f(0, 0, 0, 0));
		ParticleMaster.renderParticles(camera, new Vector4f(0, 0, 0, 0));
		TexturedParticleMaster.renderParticles(camera);
		camera.getPosition().y = cameraHeight;
		camera.resetPitch();
		mapFbo.unbindFrameBuffer();

		waterFbos.bindReflectionFrameBuffer();
		float distance = 2 * (camera.getPosition().y - (-4));
		camera.getPosition().y -= distance;
		camera.invertPitch();
		renderer.renderScene(chunks, entities, lights, camera, new Vector4f(0, 1, 0, 4));
		ParticleMaster.renderParticles(camera, new Vector4f(0, 1, 0, 4));
		camera.getPosition().y += distance;
		camera.invertPitch();

		waterFbos.bindRefractionFrameBuffer();
		renderer.renderScene(chunks, entities, lights, camera, new Vector4f(0, -1, 0, 4));
		ParticleMaster.renderParticles(camera, new Vector4f(0, -1, 0, 4));
		waterFbos.unbindCurrentFrameBuffer();

		multisampleFbo.bindFrameBuffer();
		renderer.processWater(water);
		renderer.renderScene(chunks, entities, lights, camera, new Vector4f(0, 0, 0, 0));
		ParticleMaster.renderParticles(camera, new Vector4f(0, 0, 0, 0));
		TexturedParticleMaster.renderParticles(camera);
		multisampleFbo.unbindFrameBuffer();
		multisampleFbo.resolveToScreen();
		//multisampleFbo.resolveToFbo(outputFbo);
		//PostProcessing.blur(outputFbo.getColourTexture());

		guiRenderer.render(guis);
		TextMaster.render();
	}

	public static Fbo getMenuBackground() {
		return menuBackground;
	}

}