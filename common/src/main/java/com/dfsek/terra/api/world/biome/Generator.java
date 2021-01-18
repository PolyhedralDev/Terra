package com.dfsek.terra.api.world.biome;

import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.world.palette.Palette;

public interface Generator {
    /**
     * Gets the 3D noise at a pair of coordinates using the provided FastNoiseLite instance.
     *
     * @param x - The x coordinate.
     * @param y - The y coordinate.
     * @param z - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    double getNoise(int x, int y, int z);

    double getElevation(int x, int z);

    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    Palette<BlockData> getPalette(int y);

    boolean is2d();

    double get2dBase();

    NoiseSampler getBiomeNoise();

    double getElevationWeight();
}
