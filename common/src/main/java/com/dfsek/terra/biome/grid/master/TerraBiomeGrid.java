package com.dfsek.terra.biome.grid.master;

import com.dfsek.terra.api.gaea.biome.BiomeGrid;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.biome.grid.UserDefinedGrid;

public abstract class TerraBiomeGrid extends BiomeGrid {
    public TerraBiomeGrid(World w, double freq1, double freq2, int sizeX, int sizeZ) {
        super(w, freq1, freq2, sizeX, sizeZ);
    }

    public abstract UserDefinedGrid getGrid(int x, int z);

    public enum Type {
        RADIAL, STANDARD
    }
}
