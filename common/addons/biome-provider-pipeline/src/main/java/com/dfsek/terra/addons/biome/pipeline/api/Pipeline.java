package com.dfsek.terra.addons.biome.pipeline.api;

import java.util.List;

import com.dfsek.terra.api.util.cache.SeededVector2Key;


public interface Pipeline {
    BiomeChunk generateChunk(SeededVector2Key worldCoordinates);

    int getChunkSize();

    Source getSource();

    List<Stage> getStages();
}
