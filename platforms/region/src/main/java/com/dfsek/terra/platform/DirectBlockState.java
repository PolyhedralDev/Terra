package com.dfsek.terra.platform;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.state.BlockState;

public class DirectBlockState implements BlockState {
    @Override
    public Block getBlock() {
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
