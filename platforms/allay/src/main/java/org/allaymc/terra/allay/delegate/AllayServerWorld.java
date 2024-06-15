package org.allaymc.terra.allay.delegate;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;

import org.allaymc.api.world.Dimension;
import org.allaymc.terra.allay.Mapping;
import org.allaymc.terra.allay.generator.AllayGeneratorWrapper;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public record AllayServerWorld(AllayGeneratorWrapper allayGeneratorWrapper, Dimension allayDimension) implements ServerWorld {
    @Override
    public Chunk getChunkAt(int x, int z) {
        return new AllayChunk(this, allayDimension.getChunkService().getChunk(x ,z));
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        var allayBlockState = ((AllayBlockState)data).allayBlockState();
        allayDimension.setBlockState(x, y, z, allayBlockState);
    }

    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        // TODO
        return null;
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        var allayBlockState = allayDimension.getBlockState(x, y, z);
        return new AllayBlockState(allayBlockState, Mapping.blockStateBeToJe(allayBlockState));
    }

    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        // TODO
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
