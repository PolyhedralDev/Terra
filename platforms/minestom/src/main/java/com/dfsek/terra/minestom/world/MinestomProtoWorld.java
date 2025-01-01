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
import com.dfsek.terra.minestom.chunk.GeneratedChunkCache;

import com.dfsek.terra.minestom.entity.DeferredMinestomEntity;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.Block.Setter;


public class MinestomProtoWorld implements ProtoWorld {
    private final GeneratedChunkCache cache;
    private final int x;
    private final int z;
    private final ServerWorld world;
    private final Setter modifier;

    public MinestomProtoWorld(
        GeneratedChunkCache cache,
        int x,
        int z,
        ServerWorld world,
        Setter modifier
    ) {
        this.cache = cache;
        this.x = x;
        this.z = z;
        this.world = world;
        this.modifier = modifier;
    }

    @Override
    public int centerChunkX() {
        return x;
    }

    @Override
    public int centerChunkZ() {
        return z;
    }

    @Override
    public ServerWorld getWorld() {
        return world;
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        int cx = x >> 4;
        int cz = y >> 4;
        modifier.setBlock(x, y, z, (Block) data.getHandle());
        cache.at(cx, cz)
            .setBlock(x & 15, y, z & 15, data);
    }

    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        TerraMinestomWorld world = (TerraMinestomWorld) this.world;
        DeferredMinestomEntity entity = new DeferredMinestomEntity(x, y, z, entityType, world);
        world.enqueueEntitySpawn(entity);
        return entity;
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        return cache.at(chunkX, chunkZ)
            .getBlock(x & 15, y, z & 15);
    }

    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return null;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return world.getGenerator();
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return world.getBiomeProvider();
    }

    @Override
    public ConfigPack getPack() {
        return world.getPack();
    }

    @Override
    public long getSeed() {
        return world.getSeed();
    }

    @Override
    public int getMaxHeight() {
        return world.getMaxHeight();
    }

    @Override
    public int getMinHeight() {
        return world.getMinHeight();
    }

    @Override
    public Object getHandle() {
        return world;
    }
}
