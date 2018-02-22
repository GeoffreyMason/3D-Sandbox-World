package texturedParticles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import particles.Particle;
import particles.ParticleRenderer;
import renderEngine.Loader;
import terrain.Chunk;

public class TexturedParticleMaster {

	private static Map<ParticleTexture, List<TexturedParticle>> particles = new HashMap<ParticleTexture, List<TexturedParticle>>();
	private static TexturedParticleRenderer renderer;
	private static boolean collided = false;

	public static void init(Loader loader, Matrix4f projectionMatrix) {
		renderer = new TexturedParticleRenderer(loader, projectionMatrix);
	}

	public static boolean isCollided() {
		return collided;
	}

	public static void update() {
		Iterator<Entry<ParticleTexture, List<TexturedParticle>>> mapIterator = particles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			List<TexturedParticle> list = mapIterator.next().getValue();
			Iterator<TexturedParticle> iterator = list.iterator();
			while (iterator.hasNext()) {
				TexturedParticle p = iterator.next();
				boolean stillAlive = p.update();
				if (!stillAlive) {
					iterator.remove();
					if (list.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
		}
	}

	public static void renderParticles(Camera camera) {
		renderer.render(particles, camera);
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}

	public static void addParticle(TexturedParticle particle) {
		List<TexturedParticle> list = particles.get(particle.getTexture());
		if (list == null) {
			list = new ArrayList<TexturedParticle>();
			particles.put(particle.getTexture(), list);
		}
		list.add(particle);
	}

}
