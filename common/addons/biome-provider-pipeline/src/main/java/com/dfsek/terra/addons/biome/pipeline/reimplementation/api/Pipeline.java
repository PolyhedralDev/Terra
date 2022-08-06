package com.dfsek.terra.addons.biome.pipeline.reimplementation.api;

import java.util.List;

import com.dfsek.terra.addons.biome.pipeline.reimplementation.SeededVector;


public interface Pipeline {
    BiomeChunk generateChunk(SeededVector worldCoordinates);
    
    int getChunkSize();
    
    Source getSource();
    
    List<Stage> getStages();
}
