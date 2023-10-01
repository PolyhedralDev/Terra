package com.dfsek.terra.addons.biome.pipeline.v2.api;


import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;


public interface BiomeChunk {
    
    PipelineBiome get(int xInChunk, int zInChunk);
}
