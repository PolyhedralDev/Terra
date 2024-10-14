package com.dfsek.terra.allay.delegate;

import org.allaymc.api.block.property.type.BlockPropertyTypes;
import org.allaymc.api.block.tag.BlockTags;
import org.allaymc.api.block.type.BlockTypes;
import org.allaymc.api.world.generator.context.OtherChunkAccessibleContext;
import com.dfsek.terra.allay.Mapping;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;

/**
 * @author daoge_cmd
 */
public record AllayProtoWorld(AllayServerWorld allayServerWorld, OtherChunkAccessibleContext context) implements ProtoWorld {

    private static final org.allaymc.api.block.type.BlockState WATER = BlockTypes.WATER.ofState(BlockPropertyTypes.LIQUID_DEPTH.createValue(0));

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
        AllayBlockState allayBlockState = (AllayBlockState)data;
        boolean containsWater = allayBlockState.containsWater() || context.getBlockState(x, y, z).getBlockType().hasBlockTag(BlockTags.WATER);
        context.setBlockState(x, y, z, allayBlockState.allayBlockState());
        if (containsWater) context.setBlockState(x, y, z, WATER, 1);
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
        return null;
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