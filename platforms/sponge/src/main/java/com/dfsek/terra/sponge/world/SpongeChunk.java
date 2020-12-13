package com.dfsek.terra.sponge.world;

import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.block.Block;

public class SpongeChunk implements Chunk {
    private final org.spongepowered.api.world.Chunk delegate;

    public SpongeChunk(org.spongepowered.api.world.Chunk delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getX() {
        return delegate.getPosition().getX();
    }

    @Override
    public int getZ() {
        return delegate.getPosition().getZ();
    }

    @Override
    public World getWorld() {
        return new SpongeWorld(delegate.getWorld());
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return null;
    }

    @Override
    public org.spongepowered.api.world.Chunk getHandle() {
        return delegate;
    }
}
