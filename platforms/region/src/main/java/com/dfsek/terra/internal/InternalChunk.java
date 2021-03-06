package com.dfsek.terra.internal;

import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.generator.ChunkData;

public interface InternalChunk extends ChunkData, Chunk {
    @Override
    InternalWorld getWorld();
}
