package com.dfsek.terra.carving;

import com.dfsek.terra.api.platform.world.ChunkAccess;
import com.dfsek.terra.api.platform.world.World;

public interface Carver {
    void carve(World world, int chunkX, int chunkZ, ChunkAccess chunk);
}
