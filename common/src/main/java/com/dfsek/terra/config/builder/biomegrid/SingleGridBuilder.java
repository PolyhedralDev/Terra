package com.dfsek.terra.config.builder.biomegrid;

import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.grid.SingleBiomeGrid;
import com.dfsek.terra.config.base.ConfigPack;

public class SingleGridBuilder implements BiomeGridBuilder {
    private final TerraBiome biome;

    public SingleGridBuilder(TerraBiome biome) {
        this.biome = biome;
    }

    @Override
    public SingleBiomeGrid build(long seed, ConfigPack config) {
        return new SingleBiomeGrid(seed, biome);
    }
}
