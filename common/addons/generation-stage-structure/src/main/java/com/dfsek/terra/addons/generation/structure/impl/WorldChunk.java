package com.dfsek.terra.addons.generation.structure.impl;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.WritableWorld;
import com.dfsek.terra.api.world.chunk.Chunk;

import org.jetbrains.annotations.NotNull;


/**
 * Temp class that just delegates chunk operations to the world on a block-by-block basis
 */
public class WorldChunk implements Chunk {

    private final WritableWorld world;
    private final int x;
    private final int z;

    public WorldChunk(WritableWorld world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState data, boolean physics) {
        world.setBlockState(x, y, z, data, physics);
    }

    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        return world.getBlockState(x, y, z);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public ServerWorld getWorld() {
        return null;
    }

    @Override
    public Object getHandle() {
        return null;
    }
}
