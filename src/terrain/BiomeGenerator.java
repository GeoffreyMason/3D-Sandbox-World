package terrain;

import java.util.Random;

import gameState.MainPolyActive;

public class BiomeGenerator {

	private static final float AMPLITUDE = 2f;
	private static final float OCTAVES = 6;
	private static final float ROUGHNESS = 0.4f;

	private Random random = new Random();
	private int seed;
	private int xOffset = 0;
	private int zOffset = 0;
	private float minTot = 10, maxTot = 0;

	public BiomeGenerator(int gridX, int gridZ, int vertexCount, int seed) {
		this.seed = seed;
		xOffset = gridX * (vertexCount - 1);
		zOffset = gridZ * (vertexCount - 1);
	}

	public float generateBiome(int x, int z) {
		float total = 0;
		float d = (float) Math.pow(2, OCTAVES - 1);
		for (int i = 0; i < OCTAVES; i++) {
			float freq = (float) (Math.pow(2, i) / d);
			float amp = (float) Math.pow(ROUGHNESS, i) * AMPLITUDE;
			total += getInterpolatedNoise((x + xOffset) * freq, (z + zOffset) * freq) * amp;
		}

		if ((total + 0.7f) / 1.4f > maxTot)
			maxTot = (total + 0.7f) / 1.4f;
		if ((total + 0.7f) / 1.4f < minTot)
			minTot = (total + 0.7f) / 1.4f;

		// System.out.println(minTot + " / " + maxTot);
		return (total + 0.7f) / 1.4f;
	}

	private float getInterpolatedNoise(float x, float z) {
		int intX = (int) x;
		int intZ = (int) z;
		float fracX = x - intX;
		float fracZ = z - intZ;

		float v1 = getSmoothNoise(intX, intZ);
		float v2 = getSmoothNoise(intX + 1, intZ);
		float v3 = getSmoothNoise(intX, intZ + 1);
		float v4 = getSmoothNoise(intX + 1, intZ + 1);
		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);
		return interpolate(i1, i2, fracZ);
	}

	private float interpolate(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float) ((1f - Math.cos(theta)) * 0.5f);
		return a * (1f - f) + b * f;
	}

	private float getSmoothNoise(int x, int z) {
		float corners = (getNoise(x - 1, z - 1) + getNoise(x + 1, z - 1) + getNoise(x - 1, z + 1)
				+ getNoise(x + 1, z + 1)) / 16f;
		float sides = (getNoise(x - 1, z) + getNoise(x + 1, z) + getNoise(x, z - 1) + getNoise(x, z + 1)) / 8f;
		float centre = (getNoise(x, z)) / 4f;
		return corners + sides + centre;
	}

	private float getNoise(int x, int z) {
		random.setSeed(x * 47683 + z * 87234 + seed);
		return random.nextFloat() * 2f - 1f;
	}
}
