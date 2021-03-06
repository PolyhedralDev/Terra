package com.dfsek.terra.internal.impl;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.internal.InternalChunk;
import com.dfsek.terra.internal.InternalWorld;
import com.dfsek.terra.platform.GenWrapper;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class RegionWorld implements InternalWorld {
    private final long seed;
    private final GenWrapper wrapper;
    private final List<RegionChunk> chunkList;

    public RegionWorld(long seed, GenWrapper wrapper) {
        this.seed = seed;
        this.wrapper = wrapper;
        chunkList = new GlueList<>();
    }

    public List<RegionChunk> getChunkList() {
        return chunkList;
    }

    @Override
    public Object getHandle() {
        return null;
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
        return wrapper;
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
        return chunkList.parallelStream()
                .anyMatch(chunk -> chunk.getX() == x && chunk.getZ() == z);
    }

    @Override
    public File getWorldFolder() {
        return null;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return getChunkAt(x >> 4, z >> 4).getBlock(x % 16, y, z % 16);
    }

    @Override
    public Block getBlockAt(Location l) {
        return getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    @Override
    public Entity spawnEntity(Location location,
                              EntityType entityType) {
        return null;
    }

    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public InternalChunk getChunkAt(int x, int z) {
        return chunkList.parallelStream()
                .filter(chunkList1 -> chunkList1.getX() == x && chunkList1.getZ() == z)
                .findAny()
                .orElseGet(() -> {
                    RegionChunk chunk = new RegionChunk(this, x, z);
                    chunkList.add(chunk);
                    return chunk;
                });
    }
}
