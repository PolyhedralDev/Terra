package com.dfsek.terra.biome;

import org.polydev.gaea.math.FastNoiseLite;

/**
 * Class to hold noise function to determine erosion.
 */
public class ErosionNoise {
    private final double thresh;
    private final FastNoiseLite noise;
    public ErosionNoise(float freq1, double thresh, long seed) {
        FastNoiseLite main = new FastNoiseLite((int) (seed+1));
        main.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        main.setFractalType(FastNoiseLite.FractalType.PingPong);
        main.setFractalOctaves(1);
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
        return (noise.getNoise(x, z)+1)/2 <= thresh;
    }
}
