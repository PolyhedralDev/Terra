package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.bukkit.world.block.BukkitBlock;

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
        return new BukkitWorld(delegate.getWorld());
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return new BukkitBlock(delegate.getBlock(x, y, z));
    }

    @Override
    public org.bukkit.Chunk getHandle() {
        return delegate;
    }
}
