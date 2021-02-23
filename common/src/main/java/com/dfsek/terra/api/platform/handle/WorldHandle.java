package com.dfsek.terra.api.platform.handle;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.entity.EntityType;

/**
 * Interface to be implemented for world manipulation.
 */
public interface WorldHandle {
    default void setBlockData(Block block, BlockData data, boolean physics) {
        block.setBlockData(data, physics);
    }

    default BlockData getBlockData(Block block) {
        return block.getBlockData();
    }

    BlockData createBlockData(String data);

    EntityType getEntity(String id);
}
