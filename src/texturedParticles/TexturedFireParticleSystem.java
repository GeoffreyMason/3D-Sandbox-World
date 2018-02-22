package texturedParticles;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import terrain.Chunk;

public class TexturedFireParticleSystem {

	private float lifeLength;
	private float scale;
	
	private ParticleTexture texture;

	public TexturedFireParticleSystem(ParticleTexture texture, float lifeLength, float scale) {
		this.lifeLength = lifeLength;
		this.scale = scale;
		this.texture = texture;
	}

	public void generateParticles(Vector3f systemCenter, float spread, Loader loader, Chunk terrain) {
		float delta = DisplayManager.getFrameTimeSeconds();
		float particlesToCreate = 1 / lifeLength * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			emitParticle(systemCenter, spread, loader, terrain);
		}
		if (Math.random() < partialParticle) {
			emitParticle(systemCenter, spread, loader, terrain);
		}
	}

	private void emitParticle(Vector3f centre, float spread, Loader loader, Chunk terrain) {
		float dirX = ((float) Math.random() * 2f - 1f) * 0.7f;
		float dirZ = ((float) Math.random() * 2f - 1f) * 0.7f;
		Vector3f velocity = new Vector3f(0, 0, 0);
		float nx = dirX * spread;
		float nz = dirZ * spread;
		float posX = centre.x + nx;
		float posZ = centre.z + nz;
		//lifeLength = ((float) Math.sqrt(Math.pow(dirX, 2) + Math.pow(dirZ, 2)));
		//System.out.println(lifeLength);
		new TexturedFireParticle(texture, new Vector3f(posX, centre.y + 10, posZ), velocity, 0, lifeLength, 0, 0, scale, false, loader);
	}

}
