package com.dfsek.terra.addons.biome.pipeline.v2.api;


import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;


public interface Source {
    PipelineBiome get(long seed, int x, int z);

    Iterable<PipelineBiome> getBiomes();
}