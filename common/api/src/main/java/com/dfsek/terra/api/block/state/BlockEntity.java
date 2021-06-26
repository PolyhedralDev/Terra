package com.dfsek.terra.api.block.state;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.vector.Vector3;

public interface BlockEntity extends Handle {
    Vector3 getPosition();

    int getX();

    int getY();

    int getZ();

    BlockData getBlockData();

    boolean update(boolean applyPhysics);

    default void applyState(String state) {
        // Do nothing by default.
    }
}
