package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.generic.Tree;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;

public class FabricWorldHandle implements WorldHandle {
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
        return null;
    }

    @Override
    public MaterialData createMaterialData(String data) {
        return null;
    }

    @Override
    public Tree getTree(String id) {
        return null;
    }
}
