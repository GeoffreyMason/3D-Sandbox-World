package particles;

import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import terrain.BiomeGenerator;
import terrain.HeightGenerator;
import terrain.TerrainType;

public class PaintBeamParticle extends Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float speed;
	private float scale;
	private float scaleValue;
	private float elapsedTime = 0;

	public PaintBeamParticle(Vector3f position, Vector3f velocity, Vector3f colour, float gravityEffect,
			float lifeLength, float speed, float rotation, float scale, boolean additive, Loader loader) {
		super(position, velocity, colour, gravityEffect, lifeLength, speed, rotation, scale, additive, loader);
		this.position = position;
		this.velocity = new Vector3f(velocity.x, velocity.y, velocity.z);
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.speed = speed;
		this.scale = scale;
		this.scaleValue = scale;
	}

	public RawModel generateParticle(Loader loader) {
		float[] vertices = { 0, 0.5f, -0.5f, 0, 0.5f, 0, 0, -0.5f };
		// float[] vertices = {-2f, 0.25f, -2f, -0.25f, 2f, 0.25f, 2f, -0.25f};
		float[] colour = new float[vertices.length * 3 / 2];

		float n = (float) (Math.random() + 0.5f) / 1.8f;
		for (int i = 0; i < (vertices.length / 2f); i++) {
			colour[i * 3] = super.colour.x;
			colour[i * 3 + 1] = super.colour.y;
			colour[i * 3 + 2] = super.colour.z;
		}

		return loader.loadToVAO(vertices, colour);
	}

	public void projectileMotion() {
		float time = 0;
		float speed = 0;
		float theta = 0;
		float yOffset = 0;

		float distance = (float) (speed * Math.cos(theta));
		float height = (float) (0.5f * Player.GRAVITY * Math.pow(time, 2) + speed * Math.sin(theta) * time + yOffset);
	}

	public boolean update() {
		float theta = 60;
		float yOffset = 0;
		// velocity.y += (float) (0.5f * Player.GRAVITY * Math.pow(elapsedTime,
		// 2) + speed * Math.sin(theta) * elapsedTime + yOffset);
		velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(change, position, position);
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
	}
}
