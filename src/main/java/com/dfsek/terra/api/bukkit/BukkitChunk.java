package com.dfsek.terra.api.bukkit;

import com.dfsek.terra.api.generic.world.Chunk;

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
}
