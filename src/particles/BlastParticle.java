package particles;

import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import terrain.BiomeGenerator;
import terrain.HeightGenerator;
import terrain.TerrainType;

public class BlastParticle extends Particle {

	public BlastParticle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float speed,
			float rotation, float scale, boolean additive, Loader loader) {
		super(position, velocity, gravityEffect, lifeLength, speed, rotation, scale, additive, loader);
	}

	public RawModel generateParticle(Loader loader) {
		float[] vertices = { 0, 0.8f, -0.3f, 0, 0.3f, 0, 0, -0.8f };
		float[] colour = new float[vertices.length * 3 / 2];

		float n = (float) (Math.random() + 0.5f) / 1.8f;
		for (int i = 0; i < (vertices.length / 2f); i++) {
			colour[i * 3] = 1;
			colour[i * 3 + 1] = n;
			colour[i * 3 + 2] = n;
		}

		return loader.loadToVAO(vertices, colour);
	}
}