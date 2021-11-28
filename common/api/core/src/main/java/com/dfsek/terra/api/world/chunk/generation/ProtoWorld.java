package com.dfsek.terra.api.world.chunk.generation;

import com.dfsek.terra.api.world.access.WorldAccess;


public interface ProtoWorld extends WorldAccess {
    int centerChunkX();
    
    int centerChunkZ();
}
