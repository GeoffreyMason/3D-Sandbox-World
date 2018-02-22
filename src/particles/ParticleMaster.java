package particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import renderEngine.Loader;
import terrain.Chunk;

public class ParticleMaster {

	private static List<Particle> particles = new ArrayList<Particle>();
	private static ParticleRenderer renderer;
	private static boolean collided = false;

	public static void init(Loader loader, Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(loader, projectionMatrix);
	}

	public static boolean isCollided() {
		return collided;
	}

	public static List<Particle> getParticles() {
		return particles;
	}

	public static void update(Chunk terrain) {
		Iterator<Particle> iterator = particles.iterator();
		while (iterator.hasNext()) {
			Particle p = iterator.next();
			boolean stillAlive = p.update();
			if (!stillAlive) {
				iterator.remove();
			}
		}
	}

	public static void renderParticles(Camera camera, Vector4f clipPlane) {
		renderer.render(particles, camera, clipPlane);
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}

	public static void addParticle(Particle particle) {
		particles.add(particle);
	}

}
