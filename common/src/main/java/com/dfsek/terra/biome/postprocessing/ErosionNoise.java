package com.dfsek.terra.biome.postprocessing;

import com.dfsek.terra.api.gaea.math.FastNoiseLite;
import net.jafama.FastMath;

/**
 * Class to hold noise function to determine erosion.
 */
public class ErosionNoise {
    private final double thresh;
    private final FastNoiseLite noise;

    public ErosionNoise(double freq1, double thresh, int octaves, long seed) {
        FastNoiseLite main = new FastNoiseLite((int) (seed + 1));
        main.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        main.setFractalType(FastNoiseLite.FractalType.FBm);
        main.setFractalOctaves(octaves);
        main.setFrequency(freq1);
        this.thresh = thresh;
        this.noise = main;
    }

    /**
     * Get whether a location is eroded
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Whether location is eroded
     */
    public boolean isEroded(int x, int z) {
        double abs = FastMath.pow(noise.getNoise(x, z), 2);
        return abs < thresh;
    }
}
