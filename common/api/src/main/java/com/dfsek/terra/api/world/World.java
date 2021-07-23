package com.dfsek.terra.api.world;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.TerraChunkGenerator;

public interface World extends Handle {
    long getSeed();

    int getMaxHeight();

    Chunk getChunkAt(int x, int z);

    default Chunk getChunkAt(Vector3 location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    BlockState getBlockData(int x, int y, int z);

    default BlockState getBlockData(Vector3 position) {
        return getBlockData(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }

    void setBlockData(int x, int y, int z, BlockState data, boolean physics);

    default void setBlockData(int x, int y, int z, BlockState data) {
        setBlockData(x, y, z, data, false);
    }

    default void setBlockData(Vector3 position, BlockState data) {
        setBlockData(position, data, false);
    }

    default void setBlockData(Vector3 position, BlockState data, boolean physics) {
        setBlockData(position.getBlockX(), position.getBlockY(), position.getBlockZ(), data, physics);
    }

    BlockEntity getBlockState(int x, int y, int z);

    default BlockEntity getBlockState(Vector3 position) {
        return getBlockState(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }

    Entity spawnEntity(Vector3 location, EntityType entityType);

    int getMinHeight();

    TerraChunkGenerator getGenerator();

    BiomeProvider getBiomeProvider();

    WorldConfig getConfig();
}
