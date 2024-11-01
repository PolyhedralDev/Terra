package com.dfsek.terra.addons.generation.structure.impl;

import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;


public class WorldStructureLayerGrid implements StructureLayerGrid {
    @Override
    public Chunk getChunk(ProtoWorld world, int chunkX, int chunkZ, long seed) {
        return new WorldChunk(world, chunkX, chunkZ);
    }
}
