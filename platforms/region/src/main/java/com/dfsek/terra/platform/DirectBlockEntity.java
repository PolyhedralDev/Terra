package com.dfsek.terra.platform;

import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.block.state.BlockEntity;
import com.dfsek.terra.api.vector.Vector3;

public class DirectBlockEntity implements BlockEntity {
    @Override
    public Vector3 getPosition() {
        return null;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public BlockState getBlockData() {
        return null;
    }

    @Override
    public boolean update(boolean applyPhysics) {
        return false;
    }

    @Override
    public Object getHandle() {
        return null;
    }
}
