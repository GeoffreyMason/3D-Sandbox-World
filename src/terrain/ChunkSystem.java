package terrain;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import renderEngine.Loader;

public class ChunkSystem {

	private List<Chunk> chunks = new ArrayList<Chunk>();
	private Player player;
	private Loader loader;
	private Chunk currentChunk;
	
	public ChunkSystem(Loader loader, Player player) {
		this.loader = loader;
		this.player = player;
		generateChunks();
	}

	private void generateChunks() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				chunks.add(new Chunk(i, j, loader));
			}
		}
	}

	public Chunk getCurrentChunk() {
		Vector3f position = player.getPosition();
		for (Chunk chunk : chunks) {
			if (position.getX() > chunk.getX() && position.getX() < chunk.getX() + Chunk.getMapSize()) {
				if (position.getZ() > chunk.getZ() && position.getZ() < chunk.getZ() + Chunk.getMapSize()) {
					currentChunk = chunk;
					break;
				}
			}
		}
		return currentChunk;
	}

	private boolean isChunkLoaded(int gridX, int gridZ) {
		for (int i = 0; i < chunks.size(); i++) {
			Chunk chunk = chunks.get(i);
			if (chunk.getGridX() == gridX && chunk.getGridZ() == gridZ) {
				return true;
			}
		}
		return false;
	}

	private void loadChunks() {
		int chunkToBeLoaded = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (!(isChunkLoaded(currentChunk.getGridX() + i, currentChunk.getGridZ() + j))) {
					Chunk dummyTerrain = new Chunk(currentChunk.getGridX() + i, currentChunk.getGridZ() + j, loader);
					dummyTerrain.getModel().setShineDamper(10);
					dummyTerrain.getModel().setReflectivity(0.1f);
					chunks.add(dummyTerrain);
					chunkToBeLoaded +=1;
				}
			}
		}
	}

	private void unloadChunks() {
		for (int i = 0; i < chunks.size(); i++) {
			Chunk chunk = chunks.get(i);
			float posX = chunk.getX() + Chunk.getMapSize() / 2f;
			float posZ = chunk.getZ() + Chunk.getMapSize() / 2f;
			int despawnRadius = Chunk.getMapSize() * 2;

			if (posX < player.getPosition().getX() - despawnRadius || posX > player.getPosition().getX() + despawnRadius
					|| posZ < player.getPosition().getZ() - despawnRadius
					|| posZ > player.getPosition().getZ() + despawnRadius) {
				chunks.remove(chunk);
			}
		}
	}

	public void update() {
		//loadChunks();
		//unloadChunks();
	}

	public List<Chunk> getChunkList() {
		return chunks;
	}

}
