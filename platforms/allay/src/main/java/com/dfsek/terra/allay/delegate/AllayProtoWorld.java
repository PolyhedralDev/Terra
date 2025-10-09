package com.dfsek.terra.allay.delegate;

import com.dfsek.seismic.type.vector.Vector3;
import org.allaymc.api.block.property.type.BlockPropertyTypes;
import org.allaymc.api.block.data.BlockTags;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.world.generator.context.OtherChunkAccessibleContext;

import com.dfsek.terra.allay.Mapping;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;


/**
 * @author daoge_cmd
 */
public record AllayProtoWorld(AllayServerWorld allayServerWorld, OtherChunkAccessibleContext context) implements ProtoWorld {

    private static final org.allaymc.api.block.type.BlockState WATER = BlockTypes.WATER.ofState(
        BlockPropertyTypes.LIQUID_DEPTH.createValue(0));

    @Override
    public int centerChunkX() {
        return context.getCurrentChunk().getX();
    }

    @Override
    public int centerChunkZ() {
        return context.getCurrentChunk().getZ();
    }

    @Override
    public ServerWorld getWorld() {
        return allayServerWorld;
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        AllayBlockState allayBlockState = (AllayBlockState) data;
        context.setBlockState(x, y, z, allayBlockState.allayBlockState());
        if(allayBlockState.containsWater() || context.getBlockState(x, y, z).getBlockType().hasBlockTag(BlockTags.WATER)) {
            context.setBlockState(x, y, z, WATER, 1);
        }
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        org.allaymc.api.block.type.BlockState blockState = context.getBlockState(x, y, z);
        return new AllayBlockState(blockState, Mapping.blockStateBeToJe(blockState));
    }

    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return new AllayFakeEntity(Vector3.of(x, y, z), allayServerWorld);
    }

    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return new AllayBlockEntity(getBlockEntity(context, x, y, z));
    }

    // TODO: use method in OtherChunkAccessibleContext directly after bumped allay-api version to 0.14.0
    private static org.allaymc.api.blockentity.BlockEntity getBlockEntity(OtherChunkAccessibleContext context, int x, int y, int z) {
        var currentChunk = context.getCurrentChunk();
        var currentChunkX = currentChunk.getX();
        var currentChunkZ = currentChunk.getZ();
        var dimInfo = currentChunk.getDimensionInfo();

        if (x >= currentChunkX * 16 && x < currentChunkX * 16 + 16 &&
            z >= currentChunkZ * 16 && z < currentChunkZ * 16 + 16 &&
            y >= dimInfo.minHeight() && y <= dimInfo.maxHeight()) {
            return currentChunk.getBlockEntity(x & 15, y, z & 15);
        } else {
            var chunk = context.getChunkSource().getChunk(x >> 4, z >> 4);
            return chunk == null ? null : chunk.getBlockEntity(x & 15, y, z & 15);
        }
    }

    @Override
    public ChunkGenerator getGenerator() {
        return allayServerWorld.getGenerator();
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return allayServerWorld.getBiomeProvider();
    }

    @Override
    public ConfigPack getPack() {
        return allayServerWorld.getPack();
    }

    @Override
    public long getSeed() {
        return allayServerWorld.getSeed();
    }

    @Override
    public int getMaxHeight() {
        return allayServerWorld.getMaxHeight();
    }

    @Override
    public int getMinHeight() {
        return allayServerWorld.getMinHeight();
    }

    @Override
    public AllayServerWorld getHandle() {
        return allayServerWorld;
    }
}