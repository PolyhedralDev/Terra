package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.api.util.cache.SeededVector2Key;

import java.util.List;


public interface Pipeline {
    BiomeChunk generateChunk(SeededVector2Key worldCoordinates);

    int getChunkSize();

    Source getSource();

    List<Stage> getStages();
}
