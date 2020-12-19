package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.platform.Tree;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.world.WorldHandle;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import com.dfsek.terra.fabric.world.block.FabricMaterialData;
import com.dfsek.terra.fabric.world.block.data.FabricMultipleFacing;
import com.dfsek.terra.fabric.world.block.data.FabricSlab;
import com.dfsek.terra.fabric.world.block.data.FabricStairs;
import com.dfsek.terra.fabric.world.block.data.FabricWaterlogged;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.state.property.Properties;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Arrays;

public class FabricWorldHandle implements WorldHandle {
    private Transformer<String, ConfiguredFeature<?, ?>> treeTransformer;

    public void setTreeTransformer(Transformer<String, ConfiguredFeature<?, ?>> treeTransformer) {
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
    public FabricBlockData createBlockData(String data) {
        BlockArgumentParser parser = new BlockArgumentParser(new StringReader(data), true);
        try {
            BlockState state = parser.parse(true).getBlockState();
            if(state == null) throw new IllegalArgumentException("Invalid data: " + data);
            if(state.contains(Properties.STAIR_SHAPE)) return new FabricStairs(state);
            if(state.contains(Properties.SLAB_TYPE)) return new FabricSlab(state);
            if(state.getProperties().containsAll(Arrays.asList(Properties.NORTH, Properties.SOUTH, Properties.EAST, Properties.WEST)))
                return new FabricMultipleFacing(state);
            if(state.contains(Properties.WATERLOGGED)) return new FabricWaterlogged(state);
            return new FabricBlockData(state);
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public MaterialData createMaterialData(String data) {
        return new FabricMaterialData(createBlockData(data).getHandle().getBlock());
    }

    @Override
    public Tree getTree(String id) {
        return new FabricTree(treeTransformer.translate(id));
    }
}
