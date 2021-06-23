package com.dfsek.terra.api.block.state;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.block.BlockData;

public interface BlockState extends Handle {
    Block getBlock();

    int getX();

    int getY();

    int getZ();

    BlockData getBlockData();

    boolean update(boolean applyPhysics);

    default void applyState(String state) {
        // Do nothing by default.
    }
}
