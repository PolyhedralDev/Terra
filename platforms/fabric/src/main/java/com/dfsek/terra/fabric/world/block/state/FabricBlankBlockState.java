package com.dfsek.terra.fabric.world.block.state;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.fabric.world.block.FabricBlock;

public class FabricBlankBlockState implements BlockState {
    private final FabricBlock delegate;

    public FabricBlankBlockState(FabricBlock delegate) {
        this.delegate = delegate;
    }


    @Override
    public Block getHandle() {
        return delegate;
    }

    @Override
    public Block getBlock() {
        return delegate;
    }

    @Override
    public int getX() {
        return delegate.getX();
    }

    @Override
    public int getY() {
        return delegate.getY();
    }

    @Override
    public int getZ() {
        return delegate.getZ();
    }

    @Override
    public BlockData getBlockData() {
        return delegate.getBlockData();
    }

    @Override
    public boolean update(boolean applyPhysics) {
        return true;
    }
}
