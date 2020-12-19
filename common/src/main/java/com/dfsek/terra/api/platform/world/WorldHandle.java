package com.dfsek.terra.api.platform.world;

import com.dfsek.terra.api.platform.Tree;
import com.dfsek.terra.api.platform.world.block.Block;
import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.api.platform.world.block.MaterialData;

/**
 * Interface to be implemented for world manipulation.
 */
public interface WorldHandle {
    void setBlockData(Block block, BlockData data, boolean physics);

    BlockData getBlockData(Block block);

    MaterialData getType(Block block);

    BlockData createBlockData(String data);

    MaterialData createMaterialData(String data);

    Tree getTree(String id);
}
