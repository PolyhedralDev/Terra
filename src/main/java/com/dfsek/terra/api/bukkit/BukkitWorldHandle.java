package com.dfsek.terra.api.bukkit;

import com.dfsek.terra.api.generic.world.WorldHandle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BukkitWorldHandle implements WorldHandle {

    @Override
    public void setBlockData(Block block, org.bukkit.block.data.BlockData data, boolean physics) {
        block.setBlockData(data, physics);
    }

    @Override
    public org.bukkit.block.data.BlockData getBlockData(Block block) {
        return block.getBlockData();
    }

    @Override
    public Material getType(Block block) {
        return block.getType();
    }

    @Override
    public com.dfsek.terra.api.generic.BlockData createBlockData(String data) {
        return new BukkitBlockData(Bukkit.createBlockData(data));
    }
}
