package com.dfsek.terra.platform;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.platform.world.entity.EntityType;

public class RawWorldHandle implements WorldHandle {
    @Override
    public void setBlockData(Block block, BlockData data, boolean physics) {

    }

    @Override
    public BlockData getBlockData(Block block) {
        return null;
    }

    @Override
    public MaterialData getType(Block block) {
        return null;
    }

    @Override
    public BlockData createBlockData(String data) {
        return new Data(data);
    }

    @Override
    public MaterialData createMaterialData(String data) {
        return new Data(data);
    }

    @Override
    public Tree getTree(String id) {
        return new RawTree();
    }

    @Override
    public EntityType getEntity(String id) {
        return null;
    }
}
