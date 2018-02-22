package texturedParticles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;

public class TexturedParticle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float speed;
	private float rotation;
	private float scale;
	private float scaleValue;
	private float elapsedTime = 0;
	private boolean additive;

	private RawModel model;
	
	private ParticleTexture texture;
	
	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;

	public TexturedParticle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float speed,
			float rotation, float scale, boolean additive, Loader loader) {
		this.texture = texture;
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
		TexturedParticleMaster.addParticle(this);
	}
	
	public Vector2f getTexOffset1() {
		return texOffset1;
	}

	public Vector2f getTexOffset2() {
		return texOffset2;
	}

	public float getBlend() {
		return blend;
	}

	public ParticleTexture getTexture() {
		return texture;
	}

	public RawModel getModel() {
		return model;
	}

	public boolean isAdditive() {
		return additive;
	}

	public RawModel generateParticle(Loader loader) {
		//float[] vertices = { 0, 0.8f, -0.3f, 0, 0.3f, 0, 0, -0.8f };
		float[] vertices = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};

		return loader.loadToVAO(vertices, 2);
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
		updateTextureCoordInfo();
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		setScale((lifeLength - elapsedTime) / lifeLength);
		scale = scale * scaleValue;
		return elapsedTime < lifeLength;
	}
	
	protected void updateTextureCoordInfo() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	}
	
	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) column / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}
}
