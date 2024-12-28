package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;


public class MinestomProtoWorld implements ProtoWorld {
    @Override
    public int centerChunkX() {
        return 0;
    }

    @Override
    public int centerChunkZ() {
        return 0;
    }

    @Override
    public ServerWorld getWorld() {
        return null;
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {

    }

    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return null;
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return null;
    }

    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return null;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return null;
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return null;
    }

    @Override
    public ConfigPack getPack() {
        return null;
    }

    @Override
    public long getSeed() {
        return 0;
    }

    @Override
    public int getMaxHeight() {
        return 0;
    }

    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public Object getHandle() {
        return null;
    }
}
