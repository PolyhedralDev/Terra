package com.dfsek.terra.api.gaea.biome;

import com.dfsek.terra.api.gaea.math.Interpolator;
import com.dfsek.terra.api.gaea.world.palette.Palette;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.block.BlockData;

public abstract class Generator {
    /**
     * Gets the 3D noise at a pair of coordinates using the provided FastNoiseLite instance.
     *
     * @param x - The x coordinate.
     * @param y - The y coordinate.
     * @param z - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    public abstract double getNoise(World w, int x, int y, int z);

    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    public abstract Palette<BlockData> getPalette(int y);

    /**
     * Returns true if the biome should be interpolated just once, false to use advanced interpolation + blending.
     * @return Whether biome should use minimal interpolation
     */
    public abstract boolean useMinimalInterpolation();


    /**
     * Get the type of interpolation to use in this biome.
     * @return Interpolation type
     */
    public Interpolator.Type getInterpolationType() {
        return Interpolator.Type.LINEAR;
    }
}
