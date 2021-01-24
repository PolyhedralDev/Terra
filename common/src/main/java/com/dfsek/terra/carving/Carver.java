package com.dfsek.terra.carving;

import com.dfsek.terra.api.platform.world.ChunkAccess;

public interface Carver {
    void carve(int chunkX, int chunkZ, ChunkAccess chunk);
}
