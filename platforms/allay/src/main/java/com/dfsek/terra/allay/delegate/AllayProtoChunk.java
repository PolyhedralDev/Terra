package com.dfsek.terra.allay.delegate;

import org.allaymc.api.block.property.type.BlockPropertyTypes;
import org.allaymc.api.block.tag.BlockTags;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.world.chunk.UnsafeChunk;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.allay.Mapping;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;


/**
 * @author daoge_cmd
 */
public record AllayProtoChunk(UnsafeChunk allayChunk) implements ProtoChunk {
    private static final org.allaymc.api.block.type.BlockState WATER = BlockTypes.WATER.ofState(
        BlockPropertyTypes.LIQUID_DEPTH.createValue(0)
    );

    @Override
    public int getMaxHeight() {
        return allayChunk.getDimensionInfo().maxHeight();
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        AllayBlockState allayBlockState = (AllayBlockState) blockState;
        allayChunk.setBlockState(x, y, z, allayBlockState.allayBlockState());
        if(allayBlockState.containsWater() || allayChunk.getBlockState(x, y, z).getBlockType().hasBlockTag(BlockTags.WATER)) {
            allayChunk.setBlockState(x, y, z, WATER, 1);
        }
    }

    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        org.allaymc.api.block.type.BlockState blockState = allayChunk.getBlockState(x, y, z);
        return new AllayBlockState(blockState, Mapping.blockStateBeToJe(blockState));
    }

    @Override
    public UnsafeChunk getHandle() {
        return allayChunk;
    }
}
