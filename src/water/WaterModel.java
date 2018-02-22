package water;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector2f;

import models.RawModel;
import renderEngine.Loader;
import terrain.BiomeGenerator;
import terrain.HeightGenerator;
import terrain.TerrainType;
import toolbox.Maths;

public class WaterModel {

	private static final float SPACING = 4f;
	private static final int VERTEX_COUNT = 100;
	private static final float MAP_SIZE = SPACING * (VERTEX_COUNT - 1);

	private float x;
	private float z;
	private RawModel model;

	private float biomeValueMin = 0;

	public WaterModel(float gridX, float gridZ, Loader loader) {
		this.x = gridX * MAP_SIZE;
		this.z = gridZ * MAP_SIZE;
		this.model = generateTerrain(loader);
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	private RawModel generateTerrain(Loader loader) {

		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] colour = new float[count * 3];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				float nx = (float) (Math.random() - 0.5f) * 4;
				float nz = (float) (Math.random() - 0.5f) * 4;
				vertices[vertexPointer * 3] = j * SPACING + nx;
				vertices[vertexPointer * 3 + 1] = -3f;
				vertices[vertexPointer * 3 + 2] = i * SPACING + nz;
				/*
				colour[vertexPointer * 3] = type.getRed() / 255f * (Terrain.heights[j][i] / 30f + 0.7f) / 1.4f;
				colour[vertexPointer * 3 + 1] = type.getGreen() / 255f * (Terrain.heights[j][i] / 30f + 0.7f) / 1.4f;
				colour[vertexPointer * 3 + 2] = type.getBlue() / 255f * (Terrain.heights[j][i] / 30f + 0.7f) / 1.4f;
				*/
				
				colour[vertexPointer * 3] = 64 / 255f;
				colour[vertexPointer * 3 + 1] = 164 / 255f;
				colour[vertexPointer * 3 + 2] = 223 / 255f;
				
				// if (Math.sqrt(Math.pow(vertices[vertexPointer * 3], 2) +
				// Math.pow(vertices[vertexPointer * 3 + 2], 2)) >
				// VERTEX_COUNT * SIZE / 2) {
				// vertices[vertexPointer * 3 + 1] = -120;
				// }
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;

				
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;

				indices[pointer++] = bottomRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
			}
		}

		return loader.loadToVAO(vertices, normals, indices, colour);
	}
}
