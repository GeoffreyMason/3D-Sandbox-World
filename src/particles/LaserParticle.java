package particles;

import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import terrain.BiomeGenerator;
import terrain.HeightGenerator;
import terrain.TerrainType;

public class LaserParticle extends Particle {
	
	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float speed;
	private float scale;
	private float scaleValue;
	private float elapsedTime = 0;

	public LaserParticle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float speed,
			float rotation, float scale, boolean additive, Loader loader) {
		super(position, velocity, gravityEffect, lifeLength, speed, rotation, scale, additive, loader);
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.speed = speed;
		this.scale = scale;
		this.scaleValue = scale;
	}

	public RawModel generateParticle(Loader loader) {
		float[] vertices = {0, 0.5f, -0.5f, 0, 0.5f, 0, 0, -0.5f};
		//float[] vertices = {-2f, 0.25f, -2f, -0.25f, 2f, 0.25f, 2f, -0.25f};
		float[] colour = new float[vertices.length * 3 / 2];

		float n = (float) (Math.random() + 0.5f) / 1.8f;
		for (int i = 0; i < (vertices.length / 2f); i++) {
			colour[i * 3] = 0;
			colour[i * 3 + 1] = 0.8f;
			colour[i * 3 + 2] = 0.8f;
		}

		return loader.loadToVAO(vertices, colour);
	}
	
	public boolean update() {
		velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(change, position, position);
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
	}
}
