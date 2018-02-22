package particles;

import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;

public class PaintParticle extends Particle {
	
	public PaintParticle(Vector3f position, Vector3f velocity, Vector3f colour, float gravityEffect, float lifeLength, float speed,
			float rotation, float scale, boolean additive, Loader loader) {
		super(position, velocity, colour, gravityEffect, lifeLength, speed, rotation, scale, additive, loader);
	}

	public RawModel generateParticle(Loader loader) {
		float x = 0.5f;
		float height = (float) Math.sqrt(3);
		float[] vertices = {-1, 0, -x, -height / 2f, -x, height / 2f, x, -height / 2f, x, height / 2f, 1, 0};
		float[] colour = new float[vertices.length * 3 / 2];

		float n = (float) (Math.random() + 0.5f) / 1.8f;
		for (int i = 0; i < (vertices.length / 2f); i++) {
			colour[i * 3] = super.colour.x;
			colour[i * 3 + 1] = super.colour.y;
			colour[i * 3 + 2] = super.colour.z;
		}

		return loader.loadToVAO(vertices, colour);
	}
	
	
}
