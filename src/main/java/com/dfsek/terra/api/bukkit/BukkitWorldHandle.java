package com.dfsek.terra.api.bukkit;

import com.dfsek.terra.api.generic.world.WorldHandle;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class BukkitWorldHandle implements WorldHandle {
    @Override
    public void setBlockData(Block block, BlockData data, boolean physics) {
        block.setBlockData(data, physics);
    }

    @Override
    public BlockData getBlockData(Block block) {
        return block.getBlockData();
    }

    @Override
    public Material getType(Block block) {
        return block.getType();
    }
}
