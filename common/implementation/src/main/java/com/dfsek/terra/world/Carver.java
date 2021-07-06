package com.dfsek.terra.world;

import com.dfsek.terra.api.world.ChunkAccess;
import com.dfsek.terra.api.world.World;

public interface Carver {
    void carve(World world, int chunkX, int chunkZ, ChunkAccess chunk);
}
