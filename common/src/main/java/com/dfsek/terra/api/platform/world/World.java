package com.dfsek.terra.api.platform.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.generator.ChunkGenerator;
import com.dfsek.terra.api.platform.world.entity.Entity;
import com.dfsek.terra.api.platform.world.entity.EntityType;

import java.io.File;
import java.util.UUID;

public interface World extends Handle {
    long getSeed();

    int getMaxHeight();

    ChunkGenerator getGenerator();

    String getName();

    UUID getUID();

    boolean isChunkGenerated(int x, int z);

    Chunk getChunkAt(int x, int z);

    File getWorldFolder();

    Block getBlockAt(int x, int y, int z);

    Block getBlockAt(Location l);

    boolean generateTree(Location l, Tree vanillaTreeType);

    Entity spawnEntity(Location location, EntityType entityType);
}
