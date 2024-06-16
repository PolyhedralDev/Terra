package org.allaymc.terra.allay.delegate;

import org.allaymc.api.world.chunk.Chunk;
import org.allaymc.terra.allay.Mapping;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.ServerWorld;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public record AllayChunk(ServerWorld world, Chunk allayChunk) implements com.dfsek.terra.api.world.chunk.Chunk {
    @Override
    public void setBlock(int x, int y, int z, BlockState data, boolean physics) {
        allayChunk.setBlockState(x, y, z, ((AllayBlockState)data).allayBlockState());
    }

    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        var blockState = allayChunk.getBlockState(x, y, z);
        return new AllayBlockState(blockState, Mapping.blockStateBeToJe(blockState));
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public ServerWorld getWorld() {
        return world;
    }

    @Override
    public Chunk getHandle() {
        return allayChunk;
    }
}
