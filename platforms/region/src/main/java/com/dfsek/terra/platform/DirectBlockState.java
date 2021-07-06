package com.dfsek.terra.platform;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.vector.Vector3;

public class DirectBlockState implements BlockState {
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
    public BlockData getBlockData() {
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
