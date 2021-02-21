package com.dfsek.terra.world.generation.math.interpolation;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import net.jafama.FastMath;

public class NoiseChunkInterpolator implements ChunkInterpolator {
    private final Interpolator3[][][] interpGrid = new Interpolator3[4][64][4];

    public NoiseChunkInterpolator(int chunkX, int chunkZ, NoiseSampler noise) {
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;

        double[][][] noiseStorage = new double[5][5][65];

        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                for(int y = 0; y < 65; y++) {
                    noiseStorage[x][z][y] = noise.getNoise((x << 2) + xOrigin, y << 2, (z << 2) + zOrigin);
                }
            }
        }

        for(int x = 0; x < 4; x++) {
            for(int z = 0; z < 4; z++) {
                for(int y = 0; y < 64; y++) {
                    interpGrid[x][y][z] = new Interpolator3(
                            noiseStorage[x][z][y],
                            noiseStorage[x + 1][z][y],
                            noiseStorage[x][z][y + 1],
                            noiseStorage[x + 1][z][y + 1],
                            noiseStorage[x][z + 1][y],
                            noiseStorage[x + 1][z + 1][y],
                            noiseStorage[x][z + 1][y + 1],
                            noiseStorage[x + 1][z + 1][y + 1]);
                }
            }
        }
    }

    private static int reRange(int value, int high) {
        return FastMath.max(FastMath.min(value, high), 0);
    }

    @Override
    public double getNoise(double x, double y, double z) {
        return interpGrid[reRange(((int) x) / 4, 3)][reRange(((int) y) / 4, 63)][reRange(((int) z) / 4, 3)].trilerp((x % 4) / 4, (y % 4) / 4, (z % 4) / 4);
    }
}
