package com.dfsek.terra.addons.biome.pipeline.v2.api;

import java.util.List;


public interface Pipeline {
    BiomeChunk generateChunk(SeededVector worldCoordinates);

    int getChunkSize();

    Source getSource();

    List<Stage> getStages();
}
