package com.dfsek.terra.biome;

import com.dfsek.terra.procgen.math.Vector2;
import org.polydev.gaea.math.FastNoiseLite;

/**
 * Offset a coordinate pair by an amount.
 */
public class CoordinatePerturb {
    private final FastNoiseLite perturbX;
    private final FastNoiseLite perturbZ;
    private final int amplitude;

    /**
     * Create a CoordinatePerturb object with a given frequency, amplitude, and seed.
     *
     * @param frequency Noise frequency
     * @param amplitude Offset amplitude
     * @param seed      Noise seed
     */
    public CoordinatePerturb(double frequency, int amplitude, long seed) {
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
