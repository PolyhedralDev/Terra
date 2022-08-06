package com.dfsek.terra.addons.biome.pipeline.reimplementation.api;


import com.dfsek.terra.addons.biome.pipeline.reimplementation.api.biome.PipelineBiome;


public interface BiomeChunk {
    
    PipelineBiome get(int xInChunk, int zInChunk);
}
