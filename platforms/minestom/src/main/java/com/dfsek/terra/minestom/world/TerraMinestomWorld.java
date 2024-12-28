package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;

import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;

import com.dfsek.terra.api.world.info.WorldProperties;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.world.DimensionType;


public final class TerraMinestomWorld implements ServerWorld, WorldProperties {
    private final Instance instance;
    private final MinestomChunkGeneratorWrapper wrapper;
    private final ConfigPack pack;
    private final long seed;
    private final DimensionType dimensionType;

    public TerraMinestomWorld(Instance instance, ConfigPack pack, long seed) {
        this.instance = instance;
        this.wrapper = new MinestomChunkGeneratorWrapper(
            pack.getGeneratorProvider().newInstance(pack),
            this
        );
        this.pack = pack;
        this.seed = seed;

        this.dimensionType = MinecraftServer.getDimensionTypeRegistry().get(instance.getDimensionType());

        instance.setGenerator(this.wrapper);
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
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
    public Object getHandle() {
        return instance;
    }
}
