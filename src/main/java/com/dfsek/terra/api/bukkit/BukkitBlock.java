package com.dfsek.terra.api.bukkit;

import com.dfsek.terra.api.generic.BlockData;
import com.dfsek.terra.api.generic.world.Block;

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
        return new BukkitBlockData(delegate.getBlockData());
    }

    @Override
    public Object getHandle() {
        return delegate;
    }
}
