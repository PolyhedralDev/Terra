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

import com.dfsek.terra.minestom.TerraMinestomPlatform;
import com.dfsek.terra.minestom.api.BlockEntityFactory;
import com.dfsek.terra.minestom.api.EntityFactory;
import com.dfsek.terra.minestom.block.MinestomBlockState;
import com.dfsek.terra.minestom.entity.MinestomEntity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.DimensionType;

import java.util.function.Consumer;


public final class TerraMinestomWorld implements ServerWorld, WorldProperties {
    private final Instance instance;
    private final ConfigPack pack;
    private final long seed;
    private final DimensionType dimensionType;
    private final MinestomChunkGeneratorWrapper wrapper;
    private final EntityFactory entityFactory;
    private final BlockEntityFactory blockEntityFactory;

    public TerraMinestomWorld(
        TerraMinestomPlatform platform,
        Instance instance,
        ConfigPack pack,
        long seed,
        EntityFactory entityFactory,
        BlockEntityFactory blockEntityFactory
    ) {
        this.instance = instance;
        this.pack = pack;
        this.seed = seed;

        this.dimensionType = MinecraftServer.getDimensionTypeRegistry().get(instance.getDimensionType());
        this.blockEntityFactory = blockEntityFactory;

        this.wrapper = new MinestomChunkGeneratorWrapper(platform, pack.getGeneratorProvider().newInstance(pack), this, pack);
        this.entityFactory = entityFactory;

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

    public void displayStats() {
        wrapper.displayStats();
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return new MinestomBlockState(instance.getBlock(x, y, z));
    }

    @Override
    public BlockEntity getBlockEntity(int x, int y, int z) {
        return blockEntityFactory.createBlockEntity(new BlockVec(x, y, z));
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
        return entityFactory;
    }

    public void enqueue(Point position, Consumer<net.minestom.server.instance.Chunk> action) {
        instance.loadChunk(position.chunkX(), position.chunkZ()).thenAccept(action);
    }
}
