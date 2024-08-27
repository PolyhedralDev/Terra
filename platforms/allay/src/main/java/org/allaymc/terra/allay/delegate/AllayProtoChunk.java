package org.allaymc.terra.allay.delegate;

import org.allaymc.api.block.property.type.BlockPropertyTypes;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.world.chunk.UnsafeChunk;
import org.allaymc.terra.allay.Mapping;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public record AllayProtoChunk(UnsafeChunk allayChunk) implements ProtoChunk {

    private static final org.allaymc.api.block.type.BlockState WATER = BlockTypes.WATER.ofState(BlockPropertyTypes.LIQUID_DEPTH.createValue(15));

    @Override
    public int getMaxHeight() {
        return allayChunk.getDimensionInfo().maxHeight();
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        var allayBlockState = (AllayBlockState)blockState;
        var containsWater = allayBlockState.containsWater();
        if (!containsWater) {
            var oldBlock = allayChunk.getBlockState(x, y, z);
            containsWater = oldBlock == BlockTypes.WATER;
        }
        allayChunk.setBlockState(x, y, z, allayBlockState.allayBlockState());
        if (containsWater) allayChunk.setBlockState(x, y, z, WATER, 1);
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
