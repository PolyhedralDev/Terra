package com.dfsek.terra.biome.grid.master;

import com.dfsek.terra.biome.grid.UserDefinedGrid;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;

public abstract class TerraBiomeGrid extends BiomeGrid {
    public TerraBiomeGrid(World w, double freq1, double freq2, int sizeX, int sizeZ) {
        super(w, freq1, freq2, sizeX, sizeZ);
    }

    public abstract UserDefinedGrid getGrid(int x, int z);

    public enum Type {
        RADIAL, STANDARD
    }
}
