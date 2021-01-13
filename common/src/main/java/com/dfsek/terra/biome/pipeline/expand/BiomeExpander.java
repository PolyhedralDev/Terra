package com.dfsek.terra.biome.pipeline.expand;

import com.dfsek.terra.api.world.biome.TerraBiome;

public interface BiomeExpander {
    TerraBiome getBetween(double x, double z, TerraBiome... others);
}
