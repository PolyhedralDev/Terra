package com.dfsek.terra.biome.postprocessing;

import com.dfsek.terra.api.math.FastNoiseLite;
import com.dfsek.terra.api.math.vector.Vector2;

/**
 * Offset a coordinate pair by an amount.
 */
public class CoordinatePerturb {
    private final FastNoiseLite perturbX;
    private final FastNoiseLite perturbZ;
    private final double amplitude;

    /**
     * Create a CoordinatePerturb object with a given frequency, amplitude, and seed.
     *
     * @param frequency Noise frequency
     * @param amplitude Offset amplitude
     * @param seed      Noise seed
     */
    public CoordinatePerturb(double frequency, double amplitude, long seed) {
        perturbX = new FastNoiseLite((int) seed);
        perturbX.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        perturbX.setFrequency(frequency);
        perturbZ = new FastNoiseLite((int) seed + 1);
        perturbZ.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        perturbZ.setFrequency(frequency);
        this.amplitude = amplitude;
    }

    /**
     * Offset a coordinate pair
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Vector2 containing offset coordinates
     */
    public Vector2 getShiftedCoords(int x, int z) {
        return new Vector2(perturbX.getNoise(x, z) * amplitude + x, perturbZ.getNoise(x, z) * amplitude + z);
    }
}
