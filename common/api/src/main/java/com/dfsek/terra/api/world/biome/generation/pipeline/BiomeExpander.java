package com.dfsek.terra.api.world.biome.generation.pipeline;

import com.dfsek.terra.api.world.biome.TerraBiome;

public interface BiomeExpander {
    TerraBiome getBetween(double x, double z, TerraBiome... others);
}
