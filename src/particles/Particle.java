package particles;

import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import terrain.BiomeGenerator;
import terrain.HeightGenerator;
import terrain.TerrainType;

public class Particle {

	protected Vector3f position;
	protected Vector3f velocity;
	protected Vector3f colour;
	protected float gravityEffect;
	protected float lifeLength;
	protected float speed;
	private float rotation;
	protected float scale;
	protected float scaleValue;
	protected float elapsedTime = 0;
	private boolean additive;

	private RawModel model;

	public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float speed,
			float rotation, float scale, boolean additive, Loader loader) {
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.speed = speed;
		this.rotation = rotation;
		this.scale = scale;
		this.scaleValue = scale;
		this.additive = additive;
		this.model = generateParticle(loader);
		ParticleMaster.addParticle(this);
	}
	
	public Particle(Vector3f position, Vector3f velocity, Vector3f colour, float gravityEffect, float lifeLength, float speed,
			float rotation, float scale, boolean additive, Loader loader) {
		this.position = position;
		this.velocity = velocity;
		this.colour = colour;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.speed = speed;
		this.rotation = rotation;
		this.scale = scale;
		this.scaleValue = scale;
		this.additive = additive;
		this.model = generateParticle(loader);
		ParticleMaster.addParticle(this);
	}

	public RawModel getModel() {
		return model;
	}

	public boolean isAdditive() {
		return additive;
	}

	public RawModel generateParticle(Loader loader) {
		float[] vertices = { 0, 0.8f, -0.3f, 0, 0.3f, 0, 0, -0.8f };
		float[] colour = new float[vertices.length * 3 / 2];

		float n = (float) (Math.random() + 0.5f) / 1.8f;
		for (int i = 0; i < (vertices.length / 2f); i++) {
			colour[i * 3] = 1;
			colour[i * 3 + 1] = n;
			colour[i * 3 + 2] = 0;
		}

		return loader.loadToVAO(vertices, colour);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public boolean update() {
		velocity.x += (float) (Math.random() - 0.5f) * 0.5f;
		velocity.z += (float) (Math.random() - 0.5f) * 0.5f;
		velocity.normalise();
		velocity.scale(speed);
		velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(change, position, position);
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		setScale((lifeLength - elapsedTime) / lifeLength);
		scale = scale * scaleValue;
		return elapsedTime < lifeLength;
	}
}
