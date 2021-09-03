package com.dfsek.terra.bukkit.world;

import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;


public class BukkitChunk implements Chunk {
    private final org.bukkit.Chunk delegate;
    
    public BukkitChunk(org.bukkit.Chunk delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public org.bukkit.Chunk getHandle() {
        return delegate;
    }
    
    @Override
    public void setBlock(int x, int y, int z, BlockState data, boolean physics) {
        delegate.getBlock(x, y, z).setBlockData(BukkitAdapter.adapt(data), physics);
    }
    
    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        delegate.getBlock(x, y, z).setBlockData(BukkitAdapter.adapt(blockState));
    }
    
    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        return BukkitAdapter.adapt(delegate.getBlock(x, y, z).getBlockData());
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
}
