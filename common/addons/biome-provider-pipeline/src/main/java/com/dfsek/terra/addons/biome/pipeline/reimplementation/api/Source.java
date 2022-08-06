package com.dfsek.terra.addons.biome.pipeline.reimplementation.api;


import com.dfsek.terra.addons.biome.pipeline.reimplementation.api.biome.PipelineBiome;


public interface Source {
    PipelineBiome get(long seed, int x, int z);
    
    Iterable<PipelineBiome> getBiomes();
}