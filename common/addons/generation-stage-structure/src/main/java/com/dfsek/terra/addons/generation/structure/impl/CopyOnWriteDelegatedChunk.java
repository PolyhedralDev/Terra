package com.dfsek.terra.addons.generation.structure.impl;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.chunk.Chunk;

import org.jetbrains.annotations.NotNull;


public class CopyOnWriteDelegatedChunk implements Chunk {

    private Chunk delegate;
    private boolean writeOccurred;

    public CopyOnWriteDelegatedChunk(Chunk delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState data, boolean physics) {
        if (!writeOccurred) {
            writeOccurred = true;
            delegate = new OverlayChunk(delegate);
        }
        // TODO - Tiered caching
        //  First sparse impl using hashmap, if number of set blocks exceeds certain threshold,
        //  then swap delegate to vertically segmented chunk using BlockState[vertChunk][x][z][y]
        delegate.setBlock(x, y, z, data, physics);
    }

    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        return delegate.getBlock(x, y, z);
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
    public ServerWorld getWorld() {
        return delegate.getWorld();
    }

    @Override
    public Object getHandle() {
        return delegate.getHandle();
    }

    public static class OverlayChunk implements Chunk {

        private final Chunk delegate;
        private final int worldMinHeight;
        private final BlockState[][][] blocks;

        public OverlayChunk(Chunk delegate) {
            this.delegate = delegate;
            ServerWorld world = delegate.getWorld();
            this.worldMinHeight = world.getMinHeight();
            this.blocks = new BlockState[16][16][world.getMaxHeight() - worldMinHeight];
        }

        @Override
        public void setBlock(int x, int y, int z, BlockState data, boolean physics) {
            blocks[x][z][y - worldMinHeight] = data;
        }

        @Override
        public @NotNull BlockState getBlock(int x, int y, int z) {
            BlockState state = blocks[x][z][y - worldMinHeight];
            if (state == null) return delegate.getBlock(x, y, z);
            return state;
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
        public ServerWorld getWorld() {
            return delegate.getWorld();
        }

        @Override
        public Object getHandle() {
            return delegate.getHandle();
        }
    }
}
