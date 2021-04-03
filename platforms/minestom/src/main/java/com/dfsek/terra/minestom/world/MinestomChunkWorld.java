package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.minestom.generator.MinestomChunkGenerator;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.utils.BlockPosition;

import java.io.File;
import java.util.UUID;

public class MinestomChunkWorld implements World, MinestomChunkAccess {
    private final ChunkBatch batch;
    private final net.minestom.server.instance.Chunk chunk;
    private final Instance world;

    public MinestomChunkWorld(ChunkBatch batch, net.minestom.server.instance.Chunk chunk, Instance world) {
        this.batch = batch;
        this.chunk = chunk;
        this.world = world;
    }

    @Override
    public ChunkBatch getHandle() {
        return batch;
    }

    @Override
    public int getX() {
        return chunk.getChunkX();
    }

    @Override
    public int getZ() {
        return chunk.getChunkZ();
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return new MinestomBlock(net.minestom.server.instance.block.Block.fromStateId(chunk.getBlockStateId(x, y, z)), new BlockPosition((chunk.getChunkX() << 4) + x, y, (chunk.getChunkZ() << 4) + z), Either.left(this));
    }

    @Override
    public World getWorld() {
        return new MinestomWorld(world);
    }

    @Override
    public long getSeed() {
        return 0;
    }

    @Override
    public int getMaxHeight() {
        return 255;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new MinestomChunkGenerator(world.getChunkGenerator());
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public UUID getUID() {
        return world.getUniqueId();
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return false;
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return null;
    }

    @Override
    public File getWorldFolder() {
        return null;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return new MinestomBlock(net.minestom.server.instance.block.Block.fromStateId(chunk.getBlockStateId(x - (getX() << 4), y, z - (getZ() << 4))), new BlockPosition(x, y, z), Either.left(this));
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        return null;
    }

    @Override
    public int getMinHeight() {
        return 0;
    }
}
