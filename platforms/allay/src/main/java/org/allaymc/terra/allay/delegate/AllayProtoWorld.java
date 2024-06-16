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

import org.allaymc.api.world.chunk.UnsafeChunk;
import org.allaymc.terra.allay.Mapping;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public record AllayProtoWorld(AllayServerWorld allayServerWorld, UnsafeChunk centerChunk) implements ProtoWorld {

    @Override
    public int centerChunkX() {
        return centerChunk.getX();
    }

    @Override
    public int centerChunkZ() {
        return centerChunk.getZ();
    }

    @Override
    public ServerWorld getWorld() {
        return allayServerWorld;
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        if(isInRegin(x, y, z)) {
            centerChunk.setBlockState(x & 15, y, z & 15, ((AllayBlockState)data).allayBlockState());
        }
    }

    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        // TODO
        return null;
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        if(isInRegin(x, y, z)) {
            var blockState = centerChunk.getBlockState(x & 15, y, z & 15);
            return new AllayBlockState(blockState, Mapping.blockStateBeToJe(blockState));
        }
        return AllayBlockState.AIR;
    }

    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        // TODO
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

    private boolean isInRegin(int x, int y, int z) {
        return
            x >= centerChunkX() && x < centerChunkX() + 16 &&
            z >= centerChunkZ() && z < centerChunkZ() + 16 &&
            y >= getMinHeight() && y <= getMaxHeight();
    }
}