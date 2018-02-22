package terrain;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import gameState.MainPolyActive;
import models.RawModel;
import renderEngine.Loader;
import toolbox.Maths;

public class Chunk {

	private static final float SPACING = 4f;
	private static final int VERTEX_COUNT = 100;
	private static final int MAP_SIZE = (int) (SPACING * (VERTEX_COUNT - 1));

	private int x;
	private int z;
	private int gridX;
	private int gridZ;
	private TerrainType[][] types = new TerrainType[VERTEX_COUNT][VERTEX_COUNT];
	private RawModel model;
	public int seed = MainPolyActive.seed;
	private Random random = new Random(seed);

	private HeightGenerator generator;
	private BiomeGenerator generatorBiome;

	public float[][] heights;
	public float[] positions;
	public float[] colours;

	public Chunk(int gridX, int gridZ, Loader loader) {
		this.gridX = gridX;
		this.gridZ = gridZ;
		this.x = gridX * getMapSize();
		this.z = gridZ * getMapSize();
		this.generator = new HeightGenerator(gridX, gridZ, VERTEX_COUNT, MainPolyActive.seed);
		this.generatorBiome = new BiomeGenerator(gridX, gridZ, VERTEX_COUNT, MainPolyActive.seed);
		this.types = generateTerrainType();
		this.model = generateTerrain(loader);
		this.model.setShineDamper(10);
		this.model.setReflectivity(0.1f);
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

	public Vector3f getTerrainColour(Vector2f position) {
		int vertex = (int) ((position.x + position.y * (VERTEX_COUNT - 1)) / SPACING);
		Vector3f colour = new Vector3f(colours[vertex * 3], colours[vertex * 3 + 1], colours[vertex * 3 + 2]);
		return colour;
	}

	public float[] updateTerrainColour(Vector3f paintColour, Vector2f position, int spread) {
		float r = paintColour.x;
		float g = paintColour.y;
		float b = paintColour.z;

		int vertex = (int) ((position.x + (int) position.y * (VERTEX_COUNT - 1)) / SPACING);

		int minX = (int) (vertex - spread / SPACING * (VERTEX_COUNT - 1));
		int maxX = (int) (vertex + spread / SPACING * (VERTEX_COUNT - 1));
		if (minX < 0)
			minX = 0;
		if (maxX > colours.length / 3 - VERTEX_COUNT + 1)
			maxX = colours.length / 3 - VERTEX_COUNT + 1;

		int minZ = (int) ((int) position.x / SPACING - spread);
		int maxZ = (int) ((int) position.x / SPACING + spread);
		if (minZ < 0)
			minZ = 0;
		if (maxZ > (VERTEX_COUNT))
			maxZ = (VERTEX_COUNT);

		for (int i = minX; i < maxX; i++) {
			for (int j = minZ; j < maxZ; j++) {
				float distance = (float) Math.sqrt(Math.pow(positions[(i + j) * 3] - position.x, 2)
						+ Math.pow(positions[(i + j) * 3 + 2] - position.y, 2));

				if (distance < spread) {
					colours[(i + j) * 3] = r;
					colours[(i + j) * 3 + 1] = g;
					colours[(i + j) * 3 + 2] = b;
				}
			}
		}
		return colours;
	}

	public float[] updateTerrainPosition(Vector2f position, int spread, float miningSpeed) {
		int vertex = (int) ((position.x + position.y * (VERTEX_COUNT - 1)) / SPACING);

		int minX = (int) (vertex - spread / SPACING * (VERTEX_COUNT - 1));
		int maxX = (int) (vertex + spread / SPACING * (VERTEX_COUNT - 1));
		if (minX < 0)
			minX = 0;
		if (maxX > positions.length / 3 - VERTEX_COUNT + 1)
			maxX = positions.length / 3 - VERTEX_COUNT + 1;

		int minZ = (int) ((int) position.x / SPACING - spread);
		int maxZ = (int) ((int) position.x / SPACING + spread);
		if (minZ < 0)
			minZ = 0;
		if (maxZ > (VERTEX_COUNT))
			maxZ = (VERTEX_COUNT);

		for (int i = minX; i < maxX; i++) {
			for (int j = minZ; j < maxZ; j++) {
				float distance = (float) Math.sqrt(Math.pow(positions[(i + j) * 3] - position.x, 2)
						+ Math.pow(positions[(i + j) * 3 + 2] - position.y, 2));

				if (distance < spread) {
					positions[(i + j) * 3 + 1] += miningSpeed;
					heights[j][(i - j) / (VERTEX_COUNT - 1)] = positions[(i + j) * 3 + 1];
				}
			}
		}
		return positions;
	}

	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = getMapSize() / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1 - zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}
		return answer;
	}

	private TerrainType[][] generateTerrainType() {

		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				float height = getNormalisedHeight(j, i, generator);
				float moisture = getBiomeValue(j, i, generatorBiome);
					types[i][j] = TerrainType.Sand;
					  if (height < 0.5) types[i][j] = TerrainType.Sand;
					  else types[i][j] = TerrainType.Dirt;
					  //else if (height < 0.7) types[i][j] = TerrainType.PaleGrass;
					  //else if (height < 0.8) types[i][j] = TerrainType.Grass;
					  //else types[i][j] = TerrainType.Snow;
			}
		}
		return types;
	}

	private RawModel generateTerrain(Loader loader) {

		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		positions = new float[count * 3];
		colours = new float[count * 3];
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] colour = new float[count * 3];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				
				float nx = (random.nextFloat() - 0.5f) * 4;
				float nz = (random.nextFloat() - 0.5f) * 4;
				
				if (i == 0 || i == VERTEX_COUNT - 1) {
					nx = 0;
					nz = 0;
				}
				if (j == 0 || j == VERTEX_COUNT - 1) {
					nx = 0;
					nz = 0;
				}
				
				vertices[vertexPointer * 3] = j * SPACING + nx;
				float height = getHeight(j, i, generator);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = i * SPACING + nz;
				Vector3f normal = calculateNormal(j, i, generator);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				float diff = (float) (height * 100 - Math.floor(height * 100));

				colour[vertexPointer * 3] = getColour(i, j, "red") / 255f;
				colour[vertexPointer * 3 + 1] = getColour(i, j, "green") / 255f;
				colour[vertexPointer * 3 + 2] = getColour(i, j, "blue") / 255f;

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
		positions = vertices;
		colours = colour;
		return loader.loadToVAOVBO(vertices, normals, indices, colour);
	}

	private float getColour(int x, int z, String colour) {
		float total = 0;
		if ((x > 0 && x < VERTEX_COUNT - 1 && z > 0 && z < VERTEX_COUNT - 1)) {
			if (colour == "red") {
				float corners = (types[x - 1][z - 1].getRed() + types[x + 1][z - 1].getRed()
						+ types[x - 1][z + 1].getRed() + types[x + 1][z + 1].getRed()) / 16f;
				float sides = (types[x - 1][z].getRed() + types[x + 1][z].getRed() + types[x][z - 1].getRed()
						+ types[x][z + 1].getRed()) / 8f;
				float centre = (types[x][z].getRed()) / 4f;
				total = corners + sides + centre;
			} else if (colour == "green") {
				float corners = (types[x - 1][z - 1].getGreen() + types[x + 1][z - 1].getGreen()
						+ types[x - 1][z + 1].getGreen() + types[x + 1][z + 1].getGreen()) / 16f;
				float sides = (types[x - 1][z].getGreen() + types[x + 1][z].getGreen() + types[x][z - 1].getGreen()
						+ types[x][z + 1].getGreen()) / 8f;
				float centre = (types[x][z].getGreen()) / 4f;
				total = corners + sides + centre;
			} else {
				float corners = (types[x - 1][z - 1].getBlue() + types[x + 1][z - 1].getBlue()
						+ types[x - 1][z + 1].getBlue() + types[x + 1][z + 1].getBlue()) / 16f;
				float sides = (types[x - 1][z].getBlue() + types[x + 1][z].getBlue() + types[x][z - 1].getBlue()
						+ types[x][z + 1].getBlue()) / 8f;
				float centre = (types[x][z].getBlue()) / 4f;
				total = corners + sides + centre;
			}
		} else {
			if (colour == "red") {
				total = types[x][z].getRed();
			} else if (colour == "green") {
				total = types[x][z].getGreen();
			} else
				total = types[x][z].getBlue();
		}
		return total;
	}

	private float getColourExtended(int x, int z, String colour) {
		float total = 0;
		if ((x > 1 && x < VERTEX_COUNT - 2 && z > 1 && z < VERTEX_COUNT - 2)) {
			if (colour == "red") {
				float outCorners = (types[x - 2][z - 2].getRed() + types[x + 2][z - 2].getRed()
						+ types[x - 2][z + 2].getRed() + types[x + 2][z + 2].getRed()) / 16f;
				float outSides = (types[x - 2][z].getRed() + types[x + 2][z].getRed() + types[x][z - 2].getRed()
						+ types[x][z + 2].getRed()) / 16f;
				float offSides = (types[x - 2][z - 1].getRed() + types[x - 2][z + 1].getRed()
						+ types[x + 2][z - 1].getRed() + types[x + 2][z + 1].getRed()) / 16f;
				float offSides2 = (types[x - 1][z - 2].getRed() + types[x - 1][z + 2].getRed()
						+ types[x + 1][z - 2].getRed() + types[x + 1][z + 2].getRed()) / 16f;
				float corners = (types[x - 1][z - 1].getRed() + types[x + 1][z - 1].getRed()
						+ types[x - 1][z + 1].getRed() + types[x + 1][z + 1].getRed()) / 16f;
				float sides = (types[x - 1][z].getRed() + types[x + 1][z].getRed() + types[x][z - 1].getRed()
						+ types[x][z + 1].getRed()) / 8f;
				float centre = (types[x][z].getRed()) / 4f;
				total = (corners + sides + centre) * 3 / 4
						+ (outCorners * 1 / 2 + outSides * 2 + (offSides + offSides2) * 3 / 4) * 1 / 4;
			} else if (colour == "green") {
				float outCorners = (types[x - 2][z - 2].getGreen() + types[x + 2][z - 2].getGreen()
						+ types[x - 2][z + 2].getGreen() + types[x + 2][z + 2].getGreen()) / 16f;
				float outSides = (types[x - 2][z].getGreen() + types[x + 2][z].getGreen() + types[x][z - 2].getGreen()
						+ types[x][z + 2].getGreen()) / 16f;
				float offSides = (types[x - 2][z - 1].getGreen() + types[x - 2][z + 1].getGreen()
						+ types[x + 2][z - 1].getGreen() + types[x + 2][z + 1].getGreen()) / 16f;
				float offSides2 = (types[x - 1][z - 2].getGreen() + types[x - 1][z + 2].getGreen()
						+ types[x + 1][z - 2].getGreen() + types[x + 1][z + 2].getGreen()) / 16f;
				float corners = (types[x - 1][z - 1].getGreen() + types[x + 1][z - 1].getGreen()
						+ types[x - 1][z + 1].getGreen() + types[x + 1][z + 1].getGreen()) / 16f;
				float sides = (types[x - 1][z].getGreen() + types[x + 1][z].getGreen() + types[x][z - 1].getGreen()
						+ types[x][z + 1].getGreen()) / 8f;
				float centre = (types[x][z].getGreen()) / 4f;
				total = (corners + sides + centre) * 3 / 4
						+ (outCorners * 1 / 2 + outSides * 2 + (offSides + offSides2) * 3 / 4) * 1 / 4;
			} else if (colour == "blue") {
				float outCorners = (types[x - 2][z - 2].getBlue() + types[x + 2][z - 2].getBlue()
						+ types[x - 2][z + 2].getBlue() + types[x + 2][z + 2].getBlue()) / 16f;
				float outSides = (types[x - 2][z].getBlue() + types[x + 2][z].getBlue() + types[x][z - 2].getBlue()
						+ types[x][z + 2].getBlue()) / 16f;
				float offSides = (types[x - 2][z - 1].getBlue() + types[x - 2][z + 1].getBlue()
						+ types[x + 2][z - 1].getBlue() + types[x + 2][z + 1].getBlue()) / 16f;
				float offSides2 = (types[x - 1][z - 2].getBlue() + types[x - 1][z + 2].getBlue()
						+ types[x + 1][z - 2].getBlue() + types[x + 1][z + 2].getBlue()) / 16f;
				float corners = (types[x - 1][z - 1].getBlue() + types[x + 1][z - 1].getBlue()
						+ types[x - 1][z + 1].getBlue() + types[x + 1][z + 1].getBlue()) / 16f;
				float sides = (types[x - 1][z].getBlue() + types[x + 1][z].getBlue() + types[x][z - 1].getBlue()
						+ types[x][z + 1].getBlue()) / 8f;
				float centre = (types[x][z].getBlue()) / 4f;
				total = (corners + sides + centre) * 3 / 4
						+ (outCorners * 1 / 2 + outSides * 2 + (offSides + offSides2) * 3 / 4) * 1 / 4;
			}
		}
		return total;
	}

	private Vector3f calculateNormal(int x, int z, HeightGenerator generator) {
		float heightL = getHeight(x - 1, z, generator);
		float heightR = getHeight(x + 1, z, generator);
		float heightD = getHeight(x, z - 1, generator);
		float heightU = getHeight(x, z + 1, generator);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	private float getHeight(int x, int z, HeightGenerator generator) {
		return generator.generateHeight(x, z);
	}

	private float getBiomeValue(int x, int z, BiomeGenerator generator) {
		return generator.generateBiome(x, z);
	}

	private float getNormalisedHeight(int x, int z, HeightGenerator generator) {
		// System.out.println(((generator.generateNormalisedHeight(x, z)) +
		// 0.7f) / 1.4f);
		return ((generator.generateNormalisedHeight(x, z)) + 0.7f) / 1.4f;
	}

	public static int getMapSize() {
		return MAP_SIZE;
	}

	public int getGridX() {
		return gridX;
	}

	public int getGridZ() {
		return gridZ;
	}

}
