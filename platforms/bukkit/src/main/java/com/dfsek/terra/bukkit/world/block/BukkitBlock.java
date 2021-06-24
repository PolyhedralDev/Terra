package com.dfsek.terra.bukkit.world.block;

import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.BlockFace;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;
import com.dfsek.terra.bukkit.world.block.state.BukkitBlockState;
import com.dfsek.terra.vector.LocationImpl;

public class BukkitBlock implements Block {
    private final org.bukkit.block.Block delegate;

    public BukkitBlock(org.bukkit.block.Block delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setBlockData(BlockData data, boolean physics) {
        delegate.setBlockData(((BukkitBlockData) data).getHandle(), physics);
    }

    @Override
    public BlockData getBlockData() {
        return BukkitBlockData.newInstance(delegate.getBlockData());
    }

    @Override
    public BlockState getState() {
        return BukkitBlockState.newInstance(delegate.getState());
    }

    @Override
    public Block getRelative(BlockFace face) {
        return new BukkitBlock(delegate.getRelative(BukkitAdapter.adapt(face)));
    }

    @Override
    public Block getRelative(BlockFace face, int len) {
        return new BukkitBlock(delegate.getRelative(BukkitAdapter.adapt(face), len));
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public LocationImpl getLocation() {
        return BukkitAdapter.adapt(delegate.getLocation());
    }

    @Override
    public BlockType getType() {
        return BukkitAdapter.adapt(delegate.getType());
    }

    @Override
    public int getX() {
        return delegate.getX();
    }

    @Override
    public int getZ() {
        return delegate.getZ();
    }

    @Override
    public int getY() {
        return delegate.getY();
    }

    @Override
    public boolean isPassable() {
        return delegate.isPassable();
    }

    @Override
    public org.bukkit.block.Block getHandle() {
        return delegate;
    }
}
