package com.dfsek.terra.addons.generation.structure.impl;

import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;


public interface StructureLayerGrid {
    Chunk getChunk(ProtoWorld world, int chunkX, int chunkZ, long seed);
}
