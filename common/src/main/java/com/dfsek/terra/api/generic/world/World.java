package com.dfsek.terra.api.generic.world;

import com.dfsek.terra.api.generic.Entity;
import com.dfsek.terra.api.generic.Handle;
import com.dfsek.terra.api.generic.Tree;
import com.dfsek.terra.api.generic.generator.ChunkGenerator;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.vector.Location;

import java.io.File;
import java.util.UUID;
import java.util.function.Consumer;

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

    boolean generateTree(Location l, Tree vanillaTreeType); // TODO: Bukkit treetype is bad

    void spawn(Location location, Class<Entity> entity, Consumer<Entity> consumer); // TODO: Bukkit Entity is bad
}
