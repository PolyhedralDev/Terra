package com.dfsek.terra.platform;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.generator.ChunkGenerator;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.entity.Entity;
import com.dfsek.terra.api.platform.world.entity.EntityType;

import java.io.File;
import java.util.UUID;

public class DirectWorld implements World {
    private final long seed;
    private final GenWrapper generator;

    public DirectWorld(long seed, GenWrapper generator) {
        this.seed = seed;
        this.generator = generator;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public int getMaxHeight() {
        return 255;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return generator;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public UUID getUID() {
        return null;
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
        return null;
    }

    @Override
    public Block getBlockAt(Location l) {
        return null;
    }

    @Override
    public boolean generateTree(Location l, Tree vanillaTreeType) {
        return false;
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        return null;
    }

    @Override
    public Object getHandle() {
        return generator;
    }
}
