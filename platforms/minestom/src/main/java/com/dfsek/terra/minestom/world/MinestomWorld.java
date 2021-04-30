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
import net.minestom.server.utils.BlockPosition;

import java.io.File;
import java.util.UUID;

public class MinestomWorld implements World {
    private final Instance instance;

    public MinestomWorld(Instance instance) {
        this.instance = instance;
    }

    @Override
    public Instance getHandle() {
        return instance;
    }

    @Override
    public long getSeed() {
        return 2403;
    }

    @Override
    public int getMaxHeight() {
        return 255;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new MinestomChunkGenerator(instance.getChunkGenerator());
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public UUID getUID() {
        return instance.getUniqueId();
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return false;
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return null; //instance.getChunk(x, z);
    }

    @Override
    public File getWorldFolder() {
        return null;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return new MinestomBlock(instance.getBlock(x, y, z), new BlockPosition(x, y, z), Either.right(this));
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
