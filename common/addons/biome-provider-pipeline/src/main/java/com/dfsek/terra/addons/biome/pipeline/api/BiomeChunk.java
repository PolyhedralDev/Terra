package com.dfsek.terra.addons.biome.pipeline.api;


import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;


public interface BiomeChunk {
    
    PipelineBiome get(int xInChunk, int zInChunk);
}
