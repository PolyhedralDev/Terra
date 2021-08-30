package com.dfsek.terra.addons.biome.pipeline.source;

import com.dfsek.terra.api.world.biome.TerraBiome;


public interface BiomeSource {
    TerraBiome getBiome(double x, double z, long seed);
}
