package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.Tree;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.bukkit.world.block.BukkitBlockData;
import com.dfsek.terra.bukkit.world.block.BukkitMaterialData;
import com.dfsek.terra.bukkit.world.block.data.BukkitMultipleFacing;
import com.dfsek.terra.bukkit.world.block.data.BukkitStairs;
import com.dfsek.terra.bukkit.world.block.data.BukkitWaterlogged;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Stairs;

public class BukkitWorldHandle implements WorldHandle {
    private final TerraPlugin main;

    public BukkitWorldHandle(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public void setBlockData(Block block, BlockData data, boolean physics) {
        block.setBlockData(data, physics);
    }

    @Override
    public BlockData getBlockData(Block block) {
        return block.getBlockData();
    }

    @Override
    public MaterialData getType(Block block) {
        return block.getType();
    }

    @Override
    public BlockData createBlockData(String data) {
        org.bukkit.block.data.BlockData bukkitData = Bukkit.createBlockData(data);
        if(bukkitData instanceof MultipleFacing) return new BukkitMultipleFacing((MultipleFacing) bukkitData);
        if(bukkitData instanceof Stairs) return new BukkitStairs((Stairs) bukkitData);
        if(bukkitData instanceof Waterlogged) return new BukkitWaterlogged((Waterlogged) bukkitData);
        return new BukkitBlockData(Bukkit.createBlockData(data));
    }

    @Override
    public MaterialData createMaterialData(String data) {
        return new BukkitMaterialData(Material.matchMaterial(data));
    }

    @Override
    public Tree getTree(String id) {
        return new BukkitTree(TreeType.valueOf(id), main);
    }
}
