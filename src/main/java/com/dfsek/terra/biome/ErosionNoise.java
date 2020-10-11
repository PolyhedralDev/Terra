package com.dfsek.terra.biome;

import org.polydev.gaea.math.FastNoise;

/**
 * Class to hold noise function to determine erosion.
 */
public class ErosionNoise {
    private final double thresh;
    private final FastNoise noise;
    public ErosionNoise(float freq1, double thresh, int octaves, long seed) {
        FastNoise main = new FastNoise((int) (seed+1));
        main.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        main.setFractalOctaves(octaves);
        main.setFrequency(freq1);
        this.thresh = thresh;
        this.noise = main;
    }

    /**
     * Get whether a location is eroded
     * @param x X coordinate
     * @param z Z coordinate
     * @return Whether location is eroded
     */
    boolean isEroded(int x, int z) {
        double abs = Math.pow(noise.getNoise(x, z), 2);
        return abs < thresh;
    }
}
