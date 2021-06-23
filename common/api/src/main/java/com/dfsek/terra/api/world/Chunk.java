package com.dfsek.terra.api.world;

import com.dfsek.terra.api.block.Block;

public interface Chunk extends ChunkAccess {
    int getX();

    int getZ();

    World getWorld();

    Block getBlock(int x, int y, int z);
}
