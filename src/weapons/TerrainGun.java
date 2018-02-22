package weapons;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import particles.PaintBeamParticleSystem;
import particles.PaintParticleSystem;
import renderEngine.Loader;
import terrain.Chunk;

public class TerrainGun {

	private PaintParticleSystem splash;
	PaintBeamParticleSystem paintBeam;
	private Vector3f paintColour;

	public TerrainGun() {
		this.splash = new PaintParticleSystem(50, 50, 3, 1, 1);
		this.paintBeam = new PaintBeamParticleSystem(new Vector3f(0, -1, 1), 100, 1000, 0, 1, 2);
	}

	public void update(Loader loader, Chunk terrain, Vector3f centrePoint, Vector3f beamPosition,
			Vector3f laserDirection, int spread, float miningSpeed) {
		shoot(loader, terrain, centrePoint, beamPosition, laserDirection, spread, miningSpeed);
	}

	private void shoot(Loader loader, Chunk terrain, Vector3f centrePoint, Vector3f beamPosition,
			Vector3f laserDirection, int spread, float miningSpeed) {
		paintColour = terrain.getTerrainColour(new Vector2f(centrePoint.x, centrePoint.z));
		//splash.generateParticles(centrePoint, paintColour, spread, loader, terrain);
		//paintBeam.generateParticles(beamPosition, new Vector3f(0.2f, 0.7f, 1f), 0, loader, terrain);
		//paintBeam.setDirection(laserDirection);
		loader.updatePositionVBO(0, terrain.getModel(),
				terrain.updateTerrainPosition(new Vector2f(centrePoint.x, centrePoint.z), spread, miningSpeed));
	}
}
