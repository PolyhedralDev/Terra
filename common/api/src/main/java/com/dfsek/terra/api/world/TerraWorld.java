package com.dfsek.terra.api.world;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

public interface TerraWorld {
    World getWorld();

    WorldConfig getConfig();

    /**
     * Get a block at an ungenerated location
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return BlockData
     */
    BlockState getUngeneratedBlock(int x, int y, int z);

    BlockState getUngeneratedBlock(Vector3 v);
}
