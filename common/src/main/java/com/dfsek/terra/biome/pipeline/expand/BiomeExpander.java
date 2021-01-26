package com.dfsek.terra.biome.pipeline.expand;

import com.dfsek.terra.biome.TerraBiome;

public interface BiomeExpander {
    TerraBiome getBetween(double x, double z, TerraBiome... others);
}
