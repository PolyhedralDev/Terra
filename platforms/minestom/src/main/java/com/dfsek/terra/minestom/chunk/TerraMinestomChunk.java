package com.dfsek.terra.minestom.chunk;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.minestom.block.MinestomBlockState;


public class TerraMinestomChunk implements Chunk {
    private final ServerWorld world;
    private net.minestom.server.instance.Chunk delegate;

    public TerraMinestomChunk(net.minestom.server.instance.Chunk delegate, ServerWorld world) {
        this.delegate = delegate;
        this.world = world;
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState data, boolean physics) {
        delegate.setBlock(x, y, z, (Block) data.getHandle());
    }

    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        return new MinestomBlockState(delegate.getBlock(x, y, z));
    }

    @Override
    public int getX() {
        return delegate.getChunkX();
    }

    @Override
    public int getZ() {
        return delegate.getChunkZ();
    }

    @Override
    public ServerWorld getWorld() {
        return world;
    }

    @Override
    public Object getHandle() {
        return delegate;
    }
}
