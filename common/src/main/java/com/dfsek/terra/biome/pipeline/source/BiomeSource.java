package com.dfsek.terra.biome.pipeline.source;

import com.dfsek.terra.api.world.biome.Biome;

public interface BiomeSource {
    Biome getBiome(int x, int z);
}
