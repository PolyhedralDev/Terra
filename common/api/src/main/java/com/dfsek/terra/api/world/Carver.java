package com.dfsek.terra.api.world;

public interface Carver {
    void carve(World world, int chunkX, int chunkZ, ChunkAccess chunk);
}
