package com.dfsek.terra.api.platform.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;

import java.io.File;
import java.util.UUID;

public interface World extends Handle {
    long getSeed();

    int getMaxHeight();

    ChunkGenerator getGenerator();

    Chunk getChunkAt(int x, int z);

    default Chunk getChunkAt(Location location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    Block getBlockAt(int x, int y, int z);

    default Block getBlockAt(Location l) {
        return getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    Entity spawnEntity(Location location, EntityType entityType);

    int getMinHeight();

    default boolean isTerraWorld() {
        return getGenerator().getHandle() instanceof GeneratorWrapper;
    }

    default TerraChunkGenerator getTerraGenerator() {
        return ((GeneratorWrapper) getGenerator().getHandle()).getHandle();
    }
}
