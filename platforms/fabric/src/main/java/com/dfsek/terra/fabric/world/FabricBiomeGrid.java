package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.generic.world.Biome;
import com.dfsek.terra.api.generic.world.BiomeGrid;
import org.jetbrains.annotations.NotNull;

public class FabricBiomeGrid implements BiomeGrid {
    @Override
    public @NotNull Biome getBiome(int x, int z) {
        return new FabricBiome();
    }

    @Override
    public @NotNull Biome getBiome(int x, int y, int z) {
        return new FabricBiome();
    }

    @Override
    public void setBiome(int x, int z, @NotNull Biome bio) {

    }

    @Override
    public void setBiome(int x, int y, int z, @NotNull Biome bio) {

    }

    @Override
    public Object getHandle() {
        return null;
    }
}
