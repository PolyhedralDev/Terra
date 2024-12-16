package com.dfsek.terra.allay.delegate;

import org.allaymc.api.world.Dimension;

import com.dfsek.terra.allay.Mapping;
import com.dfsek.terra.allay.generator.AllayGeneratorWrapper;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;


/**
 * @author daoge_cmd
 */
public record AllayServerWorld(AllayGeneratorWrapper allayGeneratorWrapper, Dimension allayDimension) implements ServerWorld {
    @Override
    public Chunk getChunkAt(int x, int z) {
        return new AllayChunk(this, allayDimension.getChunkService().getChunk(x, z));
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        // In dimension#setBlockState() method, Water will be moved to layer 1 if it is placed at layer 0
        allayDimension.setBlockState(x, y, z, ((AllayBlockState) data).allayBlockState());
    }

    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return new AllayFakeEntity(Vector3.of(x, y, z), this);
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        org.allaymc.api.block.type.BlockState allayBlockState = allayDimension.getBlockState(x, y, z);
        return new AllayBlockState(allayBlockState, Mapping.blockStateBeToJe(allayBlockState));
    }

    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return null;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return allayGeneratorWrapper.getHandle();
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return allayGeneratorWrapper.getBiomeProvider();
    }

    @Override
    public ConfigPack getPack() {
        return allayGeneratorWrapper.getConfigPack();
    }

    @Override
    public long getSeed() {
        return allayGeneratorWrapper.getSeed();
    }

    @Override
    public int getMaxHeight() {
        return allayDimension.getDimensionInfo().maxHeight();
    }

    @Override
    public int getMinHeight() {
        return allayDimension.getDimensionInfo().minHeight();
    }

    @Override
    public Object getHandle() {
        return allayDimension;
    }
}
