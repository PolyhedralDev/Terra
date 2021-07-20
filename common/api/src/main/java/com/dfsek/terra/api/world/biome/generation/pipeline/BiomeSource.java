package com.dfsek.terra.api.world.biome.generation.pipeline;

import com.dfsek.terra.api.world.biome.TerraBiome;

public interface BiomeSource {
    TerraBiome getBiome(double x, double z, long seed);

    enum Type {
        NOISE
    }
}
