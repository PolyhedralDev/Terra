package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;

import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;

import com.dfsek.terra.api.world.info.WorldProperties;

import com.dfsek.terra.minestom.api.EntityFactory;
import com.dfsek.terra.minestom.block.MinestomBlockState;
import com.dfsek.terra.minestom.entity.DeferredMinestomEntity;
import com.dfsek.terra.minestom.entity.MinestomEntity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.DimensionType;

import java.util.ArrayList;
import java.util.HashMap;


public final class TerraMinestomWorld implements ServerWorld, WorldProperties {
    private final Instance instance;
    private final ConfigPack pack;
    private final long seed;
    private final DimensionType dimensionType;
    private final MinestomChunkGeneratorWrapper wrapper;
    private final EntityFactory factory;

    public TerraMinestomWorld(Instance instance, ConfigPack pack, long seed, EntityFactory factory) {
        this.instance = instance;
        this.pack = pack;
        this.seed = seed;

        this.dimensionType = MinecraftServer.getDimensionTypeRegistry().get(instance.getDimensionType());

        this.wrapper = new MinestomChunkGeneratorWrapper(
            pack.getGeneratorProvider().newInstance(pack),
            this
        );
        this.factory = factory;

        instance.setGenerator(this.wrapper);
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return null;
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data, boolean physics) {
        instance.setBlock(x, y, z, (Block) data.getHandle());
    }

    @Override
    public Entity spawnEntity(double x, double y, double z, EntityType entityType) {
        return MinestomEntity.spawn(x, y, z, entityType, this);
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return new MinestomBlockState(instance.getBlock(x, y, z));
    }

    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return null;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return wrapper.getGenerator();
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return pack.getBiomeProvider();
    }

    @Override
    public ConfigPack getPack() {
        return pack;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public int getMaxHeight() {
        return dimensionType.maxY();
    }

    @Override
    public int getMinHeight() {
        return dimensionType.minY();
    }

    @Override
    public Instance getHandle() {
        return instance;
    }

    public DimensionType getDimensionType() {
        return dimensionType;
    }

    public EntityFactory getEntityFactory() {
        return factory;
    }

    public void enqueueEntitySpawn(DeferredMinestomEntity deferredMinestomEntity) {
        int chunkX = deferredMinestomEntity.position().getBlockX() >> 4;
        int chunkZ = deferredMinestomEntity.position().getBlockZ() >> 4;
        instance.loadChunk(chunkX, chunkZ).thenAccept(chunk -> deferredMinestomEntity.spawn());
    }
}
