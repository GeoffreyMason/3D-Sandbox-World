package particles;

import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import terrain.Chunk;

public class PaintParticleSystem {

	private float pps;
	private float speed;
	private float gravityComplient;
	private float lifeLength;
	private float scale;
	
	private Vector3f colour;

	public PaintParticleSystem(float pps, float speed, float gravityComplient, float lifeLength, float scale) {
		this.pps = pps;
		this.speed = speed;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
		this.scale = scale;
	}

	public void generateParticles(Vector3f systemCenter, Vector3f colour, float spread, Loader loader, Chunk terrain) {
		this.colour = colour;
		float delta = DisplayManager.getFrameTimeSeconds();
		float particlesToCreate = pps * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			emitParticle(systemCenter, spread, loader, terrain);
		}
		if (Math.random() < partialParticle) {
			emitParticle(systemCenter, spread, loader, terrain);
		}
	}
	
	public void projectileMotion() {
		float time = 0;
		float speed = 0;
		float theta = 0;
		float yOffset = 0;
		
		
		
		float distance = (float) (speed * Math.cos(theta));
		float height = (float) (0.5f * Player.GRAVITY * Math.pow(time, 2) + speed * Math.sin(theta) * time + yOffset);
	}

	private void emitParticle(Vector3f centre, float spread, Loader loader, Chunk terrain) {
		float dirX = ((float) Math.random() * 2f - 1f) * 0.7f;
		float dirZ = ((float) Math.random() * 2f - 1f) * 0.7f;
		Vector3f velocity = new Vector3f(dirX, 1, dirZ);
		velocity.normalise();
		velocity.scale(speed);
		float nx = dirX * spread;
		float nz = dirZ * spread;
		float posX = centre.x + nx;
		float posZ = centre.z + nz;
		//lifeLength = ((float) Math.sqrt(Math.pow(dirX, 2) + Math.pow(dirZ, 2)));
		//System.out.println(lifeLength);
		new PaintParticle(new Vector3f(posX, centre.y, posZ), velocity, colour, gravityComplient, lifeLength, speed, 0, scale * ((float) (Math.random() + 1f)), false, loader);
	}

}
