package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.world.ChunkAccess;

public interface ChunkData extends ChunkAccess {
    /**
     * Get the maximum height for the chunk.
     * <p>
     * Setting blocks at or above this height will do nothing.
     *
     * @return the maximum height
     */
    int getMaxHeight();
}
