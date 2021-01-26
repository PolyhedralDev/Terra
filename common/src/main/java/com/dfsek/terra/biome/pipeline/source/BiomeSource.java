package com.dfsek.terra.biome.pipeline.source;

import com.dfsek.terra.biome.TerraBiome;

public interface BiomeSource {
    TerraBiome getBiome(double x, double z);
}
