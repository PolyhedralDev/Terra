package com.dfsek.terra.world.generation.math.interpolation;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.util.mutable.MutableInteger;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import net.jafama.FastMath;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Class to abstract away the Interpolators needed to generate a chunk.<br>
 * Contains method to get interpolated noise at a coordinate within the chunk.
 */
public class ChunkInterpolator3D implements ChunkInterpolator {
    private final Interpolator3[][][] interpGrid;
    private final BiFunction<Generator, Vector3, Double> noiseGetter;

    private final int min;
    private final int max;

    /**
     * Instantiates a 3D ChunkInterpolator3D at a pair of chunk coordinates.
     *
     * @param chunkX   X coordinate of the chunk.
     * @param chunkZ   Z coordinate of the chunk.
     * @param provider Biome Provider to use for biome fetching.
     */
    public ChunkInterpolator3D(World w, int chunkX, int chunkZ, BiomeProvider provider, BiFunction<Generator, Vector3, Double> noiseGetter) {
        this.noiseGetter = noiseGetter;
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;

        this.max = w.getMaxHeight();
        this.min = w.getMinHeight();
        int range = max - min + 1;

        int size = range >> 2;

        interpGrid = new Interpolator3[4][size][4];

        double[][][] noiseStorage = new double[5][5][size + 1];

        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                Generator generator = provider.getBiome(xOrigin + (x << 2), zOrigin + (z << 2)).getGenerator(w);
                Map<Generator, MutableInteger> genMap = new HashMap<>();

                int step = generator.getBlendStep();
                int blend = generator.getBlendDistance();

                for(int xi = -blend; xi <= blend; xi++) {
                    for(int zi = -blend; zi <= blend; zi++) {
                        genMap.computeIfAbsent(provider.getBiome(xOrigin + (x << 2) + (xi * step), zOrigin + (z << 2) + (zi * step)).getGenerator(w), g -> new MutableInteger(0)).increment(); // Increment by 1
                    }
                }

                for(int y = 0; y < size + 1; y++) {
                    noiseStorage[x][z][y] = computeNoise(genMap, (x << 2) + xOrigin, y << 2, (z << 2) + zOrigin);
                }
            }
        }

        for(int x = 0; x < 4; x++) {
            for(int z = 0; z < 4; z++) {
                for(int y = 0; y < size; y++) {
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

    public double computeNoise(Generator generator, double x, double y, double z) {
        return noiseGetter.apply(generator, new Vector3(x, y, z));
    }

    private static int reRange(int value, int high) {
        return FastMath.max(FastMath.min(value, high), 0);
    }

    /**
     * Gets the noise at a pair of internal chunk coordinates.
     *
     * @param x The internal X coordinate (0-15).
     * @param z The internal Z coordinate (0-15).
     * @return double - The interpolated noise at the coordinates.
     */
    @Override
    public double getNoise(double x, double y, double z) {
        return interpGrid[reRange(((int) x) / 4, 3)][FastMath.max(FastMath.min(((int) y), max), min) / 4][reRange(((int) z) / 4, 3)].trilerp((x % 4) / 4, (y % 4) / 4, (z % 4) / 4);
    }

    public double getNoise(int x, int y, int z) {
        return interpGrid[x / 4][y / 4][z / 4].trilerp((double) (x % 4) / 4, (double) (y % 4) / 4, (double) (z % 4) / 4);
    }
}
