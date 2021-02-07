package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import com.dfsek.terra.fabric.world.block.FabricMaterialData;
import com.dfsek.terra.fabric.world.block.data.FabricMultipleFacing;
import com.dfsek.terra.fabric.world.block.data.FabricOrientable;
import com.dfsek.terra.fabric.world.block.data.FabricSlab;
import com.dfsek.terra.fabric.world.block.data.FabricStairs;
import com.dfsek.terra.fabric.world.block.data.FabricWaterlogged;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.state.property.Properties;

import java.util.Arrays;

public class FabricWorldHandle implements WorldHandle {
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

            if(state.contains(Properties.AXIS)) return new FabricOrientable(state);

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
    public EntityType getEntity(String id) {
        return null;
    }
}
