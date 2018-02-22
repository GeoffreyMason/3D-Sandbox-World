package texturedParticles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;

public class TexturedFireParticle extends TexturedParticle {
	
	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float speed;
	private float scale;
	private float scaleValue;
	private float elapsedTime = 0;

	public TexturedFireParticle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect,
			float lifeLength, float speed, float rotation, float scale, boolean additive, Loader loader) {
		super(texture, position, velocity, gravityEffect, lifeLength, speed, rotation, scale, additive, loader);
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.speed = speed;
		this.scale = scale;
		this.scaleValue = scale;
	}


}
