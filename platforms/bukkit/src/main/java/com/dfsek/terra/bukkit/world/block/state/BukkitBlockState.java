package com.dfsek.terra.bukkit.world.block.state;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.state.BlockState;
import com.dfsek.terra.bukkit.world.block.BukkitBlock;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;

public class BukkitBlockState implements BlockState {
    private final org.bukkit.block.BlockState delegate;

    protected BukkitBlockState(org.bukkit.block.BlockState block) {
        this.delegate = block;
    }

    public static BukkitBlockState newInstance(org.bukkit.block.BlockState block) {
        return new BukkitBlockState(block);
    }

    @Override
    public org.bukkit.block.BlockState getHandle() {
        return delegate;
    }

    @Override
    public Block getBlock() {
        return new BukkitBlock(delegate.getBlock());
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
        return BukkitBlockData.newInstance(delegate.getBlockData());
    }

    @Override
    public boolean update(boolean applyPhysics) {
        return delegate.update(true, applyPhysics);
    }
}
