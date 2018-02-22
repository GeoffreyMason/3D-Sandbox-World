package weapons;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.PlayerInput;
import particles.PaintBeamParticleSystem;
import particles.PaintParticleSystem;
import renderEngine.Loader;
import terrain.Chunk;

public class PaintGun {

	private PaintParticleSystem splash;
	private PaintBeamParticleSystem paintBeam;
	

	public PaintGun() {
		this.splash = new PaintParticleSystem(50, 50, 3, 1, 1);
		this.paintBeam = new PaintBeamParticleSystem(new Vector3f(0, -1, 1), 100, 1000, 0, 1, 2);
	}
	
	public void update(Loader loader, Chunk terrain, Vector3f centrePoint, Vector3f beamPosition, Vector3f laserDirection, int spread, int colourID, PlayerInput input) {
		Vector3f paintColour = new Vector3f(input.getColour().getRed() / 255f, input.getColour().getGreen() / 255f, input.getColour().getBlue() / 255f);
		shoot(loader, terrain, centrePoint, beamPosition, laserDirection, spread, paintColour);
	}

	private void shoot(Loader loader, Chunk terrain, Vector3f centrePoint, Vector3f beamPosition, Vector3f laserDirection, int spread, Vector3f paintColour) {
		splash.generateParticles(centrePoint, paintColour, spread, loader, terrain);
		paintBeam.generateParticles(beamPosition, paintColour, 0, loader, terrain);
		paintBeam.setDirection(laserDirection);
		loader.updateColourVBO(0, terrain.getModel(),
				terrain.updateTerrainColour(paintColour, new Vector2f(centrePoint.x, centrePoint.z), spread));
	}
}
