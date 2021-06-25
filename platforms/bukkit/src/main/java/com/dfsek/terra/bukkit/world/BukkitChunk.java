package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
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
    public org.bukkit.Chunk getHandle() {
        return delegate;
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        delegate.getBlock(x, y, z).setBlockData(BukkitAdapter.adapt(blockData));
    }

    @Override
    public @NotNull BlockData getBlock(int x, int y, int z) {
        return BukkitAdapter.adapt(delegate.getBlock(x, y, z).getBlockData());
    }

    @Override
    public void setBlock(int x, int y, int z, BlockData data, boolean physics) {
        delegate.getBlock(x, y, z).setBlockData(BukkitAdapter.adapt(data), physics);
    }
}
