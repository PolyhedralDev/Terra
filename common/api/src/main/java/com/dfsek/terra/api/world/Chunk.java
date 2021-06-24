package com.dfsek.terra.api.world;

import com.dfsek.terra.api.block.BlockData;

public interface Chunk extends ChunkAccess {
    int getX();

    int getZ();

    World getWorld();

    BlockData getBlockData(int x, int y, int z);

    void setBlockData(int x, int y, int z, BlockData data, boolean physics);

    default void setBlockData(int x, int y, int z, BlockData data) {
        setBlockData(x, y, z, data, false);
    }
}
