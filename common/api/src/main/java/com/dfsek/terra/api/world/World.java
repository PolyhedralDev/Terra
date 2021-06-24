package com.dfsek.terra.api.world;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.generator.TerraChunkGenerator;

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
