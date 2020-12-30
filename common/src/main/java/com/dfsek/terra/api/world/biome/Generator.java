package com.dfsek.terra.api.world.biome;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.World;
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
    double getNoise(World w, int x, int y, int z);

    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    Palette<BlockData> getPalette(int y);

    /**
     * Returns true if the biome should be interpolated just once, false to use advanced interpolation + blending.
     *
     * @return Whether biome should use minimal interpolation
     */
    boolean useMinimalInterpolation();
}
