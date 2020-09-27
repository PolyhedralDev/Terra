package com.dfsek.terra.biome;

import org.polydev.gaea.math.FastNoise;

public class CoordinatePerturb {
    private final FastNoise perturbX;
    private final FastNoise perturbZ;
    private final int amplitude;
    public CoordinatePerturb(float frequency, int amplitude, long seed) {
        perturbX = new FastNoise((int) seed);
        perturbX.setNoiseType(FastNoise.NoiseType.Simplex);
        perturbX.setFrequency(frequency);
        perturbZ = new FastNoise((int) seed+1);
        perturbZ.setNoiseType(FastNoise.NoiseType.Simplex);
        perturbZ.setFrequency(frequency);
        this.amplitude = amplitude;
    }
    public int[] getShiftedCoords(int x, int z) {
        return new int[] {(int) (perturbX.getNoise(x, z)*amplitude+x), (int) (perturbZ.getNoise(x, z)*amplitude+z)};
    }
}
