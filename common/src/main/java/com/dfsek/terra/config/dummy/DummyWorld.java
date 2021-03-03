package com.dfsek.terra.config.dummy;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;

import java.io.File;
import java.util.UUID;

public class DummyWorld implements World {
    @Override
    public Object getHandle() {
        throw new UnsupportedOperationException("Cannot get handle of DummyWorld");
    }

    @Override
    public long getSeed() {
        return 2403L;
    }

    @Override
    public int getMaxHeight() {
        return 155;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return () -> (ChunkGenerator) () -> null;
    }

    @Override
    public String getName() {
        return "DUMMY";
    }

    @Override
    public UUID getUID() {
        return UUID.randomUUID();
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return false;
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        throw new UnsupportedOperationException("Cannot get chunk in DummyWorld");
    }

    @Override
    public File getWorldFolder() {
        throw new UnsupportedOperationException("Cannot get folder of DummyWorld");
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        throw new UnsupportedOperationException("Cannot get block in DummyWorld");
    }

    @Override
    public Block getBlockAt(Location l) {
        throw new UnsupportedOperationException("Cannot get block in DummyWorld");
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        throw new UnsupportedOperationException("Cannot spawn entity in DummyWorld");
    }

    @Override
    public int getMinHeight() {
        return 0;
    }
}
