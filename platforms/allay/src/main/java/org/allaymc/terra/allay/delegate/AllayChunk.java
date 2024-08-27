package org.allaymc.terra.allay.delegate;

import org.allaymc.api.block.property.type.BlockPropertyTypes;
import org.allaymc.api.block.type.BlockTypes;
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

    private static final org.allaymc.api.block.type.BlockState WATER = BlockTypes.WATER.ofState(BlockPropertyTypes.LIQUID_DEPTH.createValue(15));

    @Override
    public void setBlock(int x, int y, int z, BlockState data, boolean physics) {
        var allayBlockState = (AllayBlockState)data;
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
    public int getX() {
        return allayChunk.getX();
    }

    @Override
    public int getZ() {
        return allayChunk.getZ();
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
