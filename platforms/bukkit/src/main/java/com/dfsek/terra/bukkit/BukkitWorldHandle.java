package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.Tree;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.world.WorldHandle;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.bukkit.world.block.BukkitBlockData;
import com.dfsek.terra.bukkit.world.block.BukkitMaterialData;
import com.dfsek.terra.bukkit.world.block.data.BukkitDirectional;
import com.dfsek.terra.bukkit.world.block.data.BukkitMultipleFacing;
import com.dfsek.terra.bukkit.world.block.data.BukkitOrientable;
import com.dfsek.terra.bukkit.world.block.data.BukkitRotatable;
import com.dfsek.terra.bukkit.world.block.data.BukkitSlab;
import com.dfsek.terra.bukkit.world.block.data.BukkitStairs;
import com.dfsek.terra.bukkit.world.block.data.BukkitWaterlogged;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;

public class BukkitWorldHandle implements WorldHandle {
    private Transformer<String, Tree> treeTransformer;

    public BukkitWorldHandle(TerraPlugin main) {
    }

    public void setTreeTransformer(Transformer<String, Tree> treeTransformer) {
        this.treeTransformer = treeTransformer;
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
        if(bukkitData instanceof Slab) return new BukkitSlab((Slab) bukkitData);
        if(bukkitData instanceof Rotatable) return new BukkitRotatable((Rotatable) bukkitData);
        if(bukkitData instanceof Directional) return new BukkitDirectional((Directional) bukkitData);
        if(bukkitData instanceof Orientable) return new BukkitOrientable((Orientable) bukkitData);
        if(bukkitData instanceof Waterlogged) return new BukkitWaterlogged((Waterlogged) bukkitData);
        return new BukkitBlockData(Bukkit.createBlockData(data));
    }

    @Override
    public MaterialData createMaterialData(String data) {
        return new BukkitMaterialData(Material.matchMaterial(data));
    }

    @Override
    public Tree getTree(String id) {
        return treeTransformer.translate(id);
    }
}
