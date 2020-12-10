package com.dfsek.terra.api.generic.world;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * Interface to be implemented for world manipulation.
 */
public interface WorldHandle {
    void setBlockData(Block block, BlockData data, boolean physics);

    BlockData getBlockData(Block block);

    Material getType(Block block);

    com.dfsek.terra.api.generic.BlockData createBlockData(String data);
}
