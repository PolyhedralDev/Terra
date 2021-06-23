package com.dfsek.terra.api.world;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

public interface TerraWorld {
    World getWorld();

    BiomeProvider getBiomeProvider();

    WorldConfig getConfig();

    boolean isSafe();

    /**
     * Get a block at an ungenerated location
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return BlockData
     */
    BlockData getUngeneratedBlock(int x, int y, int z);

    BlockData getUngeneratedBlock(Location l);

    BlockData getUngeneratedBlock(Vector3 v);
}
