package com.dfsek.terra.api.platform.handle;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.platform.world.entity.EntityType;

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

    EntityType getEntity(String id);
}
