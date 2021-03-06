package com.dfsek.terra.internal.impl;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.state.BlockState;

public class RegionBlockState implements BlockState {
    private final RegionBlock block;
    private String state;

    public RegionBlockState(RegionBlock block) {
        this.block = block;
        state = "";
    }

    @Override
    public Object getHandle() {
        return state;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public int getX() {
        return block.getX();
    }

    @Override
    public int getY() {
        return block.getY();
    }

    @Override
    public int getZ() {
        return block.getZ();
    }

    @Override
    public BlockData getBlockData() {
        return block.getBlockData();
    }

    @Override
    public boolean update(boolean applyPhysics) {
        return false;
    }

    @Override
    public void applyState(String state) {
        this.state = state;
    }
}
