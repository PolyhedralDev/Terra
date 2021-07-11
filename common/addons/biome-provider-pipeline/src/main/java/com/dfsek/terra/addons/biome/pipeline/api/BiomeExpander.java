package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.api.world.biome.TerraBiome;

public interface BiomeExpander {
    TerraBiome getBetween(double x, double z, TerraBiome... others);
}
