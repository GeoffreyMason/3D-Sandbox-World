package weapons;

import org.lwjgl.util.vector.Vector3f;

import entities.PlayerInput;
import renderEngine.Loader;
import terrain.Chunk;

public class WeaponManager {
	
	private PaintGun paintGun;
	private TerrainGun terrainGun;
	
	public WeaponManager() {
		paintGun = new PaintGun();
		terrainGun = new TerrainGun();
	}
	
	public void update(Loader loader, Chunk terrain, Vector3f centrePoint, Vector3f beamPosition,
			Vector3f laserDirection, PlayerInput input) {
		if (input.getWeaponID() == 1) {
			terrainGun.update(loader, terrain, centrePoint, beamPosition, laserDirection, input.getSpread(), input.getMiningSpeed());
		}
		if (input.getWeaponID() == 2) {
			paintGun.update(loader, terrain, centrePoint, beamPosition, laserDirection, input.getSpread(), input.getColourID(), input);
		}
	}
	

}
