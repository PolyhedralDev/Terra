package com.dfsek.terra.sponge.world;

import com.dfsek.terra.api.generic.Tree;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.sponge.TerraSpongePlugin;
import com.dfsek.terra.sponge.world.block.SpongeBlockData;

public class SpongeWorldHandle implements WorldHandle {
    private final TerraSpongePlugin main;

    public SpongeWorldHandle(TerraSpongePlugin main) {
        this.main = main;
    }

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
        return new SpongeBlockData(null);
    }

    @Override
    public MaterialData createMaterialData(String data) {
        return createBlockData(data).getMaterial();
    }

    @Override
    public Tree getTree(String id) {
        return null;
    }
}
