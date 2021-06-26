package com.dfsek.terra.api.world;

import com.dfsek.terra.api.block.state.BlockState;

public interface Chunk extends ChunkAccess {
    int getX();

    int getZ();

    World getWorld();

    BlockState getBlock(int x, int y, int z);

    void setBlock(int x, int y, int z, BlockState data, boolean physics);

    default void setBlock(int x, int y, int z, BlockState data) {
        setBlock(x, y, z, data, false);
    }
}
