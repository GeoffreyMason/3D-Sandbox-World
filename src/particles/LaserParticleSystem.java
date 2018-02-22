package particles;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import terrain.Chunk;

public class LaserParticleSystem {

	private Vector3f direction;
	private float pps;
	private float speed;
	private float gravityComplient;
	private float lifeLength;
	private float scale;

	public LaserParticleSystem(Vector3f direction, float pps, float speed, float gravityComplient, float lifeLength, float scale) {
		this.pps = pps;
		this.speed = speed;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
		this.scale = scale;
		this.direction = direction;
	}
	
	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public void generateParticles(Vector3f systemCenter, float spread, Loader loader, Chunk terrain) {
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

	private void emitParticle(Vector3f centre, float spread, Loader loader, Chunk terrain) {
		float dirX = direction.x;
		float dirY = direction.y;
		float dirZ = direction.z;
		Vector3f velocity = new Vector3f(dirX, dirY, dirZ);
		velocity.normalise();
		velocity.scale(speed);
		float nx = dirX * spread;
		float nz = dirZ * spread;
		float posX = centre.x + nx;
		float posZ = centre.z + nz;
		//lifeLength = ((float) Math.sqrt(Math.pow(dirX, 2) + Math.pow(dirZ, 2)));
		//System.out.println(lifeLength);
		new LaserParticle(new Vector3f(posX, centre.y, posZ), velocity, gravityComplient, lifeLength, speed, 0, scale, true, loader);
	}

}
