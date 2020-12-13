package com.dfsek.terra.biome.grid.master;

import com.dfsek.terra.api.gaea.biome.BiomeGrid;
import com.dfsek.terra.biome.grid.UserDefinedGrid;

public abstract class TerraBiomeGrid extends BiomeGrid {
    public TerraBiomeGrid(long seed, double freq1, double freq2, int sizeX, int sizeZ) {
        super(seed, freq1, freq2, sizeX, sizeZ);
    }

    public abstract UserDefinedGrid getGrid(int x, int z);

    public enum Type {
        RADIAL, STANDARD
    }
}
