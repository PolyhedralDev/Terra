package com.dfsek.terra.api.world.chunk.generation;

import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.WritableWorld;


public interface ProtoWorld extends WritableWorld {
    int centerChunkX();
    
    int centerChunkZ();
    
    /**
     * Get the world object this ProtoWorld represents
     *
     * <b>Do not read from/write to this world!</b>
     *
     * @return The world
     */
    ServerWorld getWorld();
}
