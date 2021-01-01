package com.dfsek.terra.bukkit.handles;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.bukkit.world.block.BukkitMaterialData;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Material;

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
        return BukkitBlockData.newInstance(bukkitData);
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
