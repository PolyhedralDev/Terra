package org.allaymc.terra.allay.delegate;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;

import org.allaymc.api.world.chunk.Chunk;
import org.allaymc.api.world.chunk.UnsafeChunk;
import org.allaymc.terra.allay.Mapping;
import org.jetbrains.annotations.NotNull;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public record AllayProtoChunk(UnsafeChunk allayChunk) implements ProtoChunk {
    @Override
    public int getMaxHeight() {
        return allayChunk.getDimensionInfo().maxHeight();
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        allayChunk.setBlockState(x, y, z, ((AllayBlockState)blockState).allayBlockState());
    }

    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        var blockState = allayChunk.getBlockState(x, y, z);
        return new AllayBlockState(blockState, Mapping.blockStateBeToJe(blockState));
    }

    @Override
    public UnsafeChunk getHandle() {
        return allayChunk;
    }
}
