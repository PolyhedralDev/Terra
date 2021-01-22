package com.dfsek.terra.generation.math.interpolation;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.util.mutable.MutableInteger;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.biome.BiomeProvider;
import net.jafama.FastMath;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to abstract away the 16 Interpolators needed to generate a chunk.<br>
 * Contains method to get interpolated noise at a coordinate within the chunk.
 */
public class ChunkInterpolator {
    private final Interpolator3[][][] interpGrid = new Interpolator3[4][64][4];

    /**
     * Instantiates a 3D ChunkInterpolator at a pair of chunk coordinates.
     *
     * @param chunkX   X coordinate of the chunk.
     * @param chunkZ   Z coordinate of the chunk.
     * @param provider BiomeGrid to use for noise fetching.
     */
    public ChunkInterpolator(World w, int chunkX, int chunkZ, BiomeProvider provider) {
        Generator[][] gens = new Generator[5][5];
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;

        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                gens[x][z] = provider.getBiome(xOrigin + (x * 4), zOrigin + (z * 4)).getGenerator(w);
            }
        }

        double[][][] noiseStorage = new double[5][5][65];

        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                Generator generator = gens[x][z];
                Map<Generator, MutableInteger> genMap = new HashMap<>();

                int blend = generator.getBlendDistance();
                for(int xi = -blend; xi <= blend; xi++) {
                    for(int zi = -blend; zi <= blend; zi++) {
                        genMap.computeIfAbsent(provider.getBiome(xOrigin + ((x + xi) << 2), zOrigin + ((z + zi) << 2)).getGenerator(w), g -> new MutableInteger(0)).add(); // Increment by 1
                    }
                }

                for(int y = 0; y < 65; y++) {
                    noiseStorage[x][z][y] = computeNoise(genMap, (x << 2) + xOrigin, y << 2, (z << 2) + zOrigin);
                }
            }
        }

        for(byte x = 0; x < 4; x++) {
            for(byte z = 0; z < 4; z++) {
                for(int y = 0; y < 64; y++) {
                    interpGrid[x][y][z] = new Interpolator3(
                            biomeAvg(x, y, z, noiseStorage),
                            biomeAvg(x + 1, y, z, noiseStorage),
                            biomeAvg(x, y + 1, z, noiseStorage),
                            biomeAvg(x + 1, y + 1, z, noiseStorage),
                            biomeAvg(x, y, z + 1, noiseStorage),
                            biomeAvg(x + 1, y, z + 1, noiseStorage),
                            biomeAvg(x, y + 1, z + 1, noiseStorage),
                            biomeAvg(x + 1, y + 1, z + 1, noiseStorage));
                }
            }
        }
    }

    private static double computeNoise(Map<Generator, MutableInteger> gens, double x, double y, double z) {
        double n = 0;
        double div = 0;
        for(Map.Entry<Generator, MutableInteger> entry : gens.entrySet()) {
            Generator gen = entry.getKey();
            int weight = entry.getValue().get();
            double noise = computeNoise(gen, x, y, z);

            n += noise * weight;
            div += gen.getWeight() * weight;
        }
        return n / div;
    }

    private static double computeNoise(Generator generator, double x, double y, double z) {
        if(generator.is2d()) return generator.getNoise(x, 0, z) + noise2dExtrude(y, generator.get2dBase());
        else return generator.getNoise(x, y, z);
    }

    private static int reRange(int value, int high) {
        return FastMath.max(FastMath.min(value, high), 0);
    }

    private static double noise2dExtrude(double y, double base) {
        return ((-FastMath.pow2((y / base))) + 1);
    }

    private double biomeAvg(int x, int y, int z, double[][][] noise) {
        return noise[x][z][y];
    }

    /**
     * Gets the noise at a pair of internal chunk coordinates.
     *
     * @param x The internal X coordinate (0-15).
     * @param z The internal Z coordinate (0-15).
     * @return double - The interpolated noise at the coordinates.
     */
    public double getNoise(double x, double y, double z) {
        return interpGrid[reRange(((int) x) / 4, 3)][reRange(((int) y) / 4, 63)][reRange(((int) z) / 4, 3)].trilerp((x % 4) / 4, (y % 4) / 4, (z % 4) / 4);
    }
}
