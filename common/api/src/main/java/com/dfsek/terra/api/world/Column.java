package com.dfsek.terra.api.world;

import com.dfsek.terra.api.block.state.BlockState;

/**
 * A single vertical column of a world.
 */
public interface Column {
    int getX();

    int getZ();

    BlockState getBlock(int y);

    World getWorld();
}
