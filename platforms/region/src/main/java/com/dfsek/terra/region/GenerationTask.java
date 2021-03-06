package com.dfsek.terra.region;

import com.dfsek.terra.platform.DirectChunkData;

@FunctionalInterface
public interface GenerationTask {
    DirectChunkData generate(DirectChunkData chunk);
}
