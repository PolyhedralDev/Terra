package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.bukkit.world.block.BukkitBlock;
import org.jetbrains.annotations.NotNull;

public class BukkitChunk implements Chunk {
    private final org.bukkit.Chunk delegate;

    public BukkitChunk(org.bukkit.Chunk delegate) {
        this.delegate = delegate;
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
    public World getWorld() {
        return BukkitAdapter.adapt(delegate.getWorld());
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return new BukkitBlock(delegate.getBlock(x, y, z));
    }

    @Override
    public org.bukkit.Chunk getHandle() {
        return delegate;
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        delegate.getBlock(x, y, z).setBlockData(BukkitAdapter.adapt(blockData));
    }

    @Override
    public @NotNull BlockData getBlockData(int x, int y, int z) {
        return getBlock(x, y, z).getBlockData();
    }
}
