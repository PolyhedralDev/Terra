package org.allaymc.terra.allay.delegate;

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
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public record AllayProtoWorld(ServerWorld serverWorld, int centerChunkX, int centerChunkZ) implements ProtoWorld {

    @Override
    public ServerWorld getWorld() {
        return serverWorld;
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        serverWorld.setBlockState(x, y, z, data, physics);
    }

    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        // TODO
        return null;
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return serverWorld.getBlockState(x, y, z);
    }

    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        // TODO
        return null;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return serverWorld.getGenerator();
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return serverWorld.getBiomeProvider();
    }

    @Override
    public ConfigPack getPack() {
        return serverWorld.getPack();
    }

    @Override
    public long getSeed() {
        return serverWorld.getSeed();
    }

    @Override
    public int getMaxHeight() {
        return serverWorld.getMaxHeight();
    }

    @Override
    public int getMinHeight() {
        return serverWorld.getMinHeight();
    }

    @Override
    public ServerWorld getHandle() {
        return serverWorld;
    }
}
