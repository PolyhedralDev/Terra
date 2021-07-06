package com.dfsek.terra.api.world;

import com.dfsek.terra.api.block.BlockData;

public interface Chunk extends ChunkAccess {
    int getX();

    int getZ();

    World getWorld();

    BlockData getBlock(int x, int y, int z);

    void setBlock(int x, int y, int z, BlockData data, boolean physics);

    default void setBlock(int x, int y, int z, BlockData data) {
        setBlock(x, y, z, data, false);
    }
}
