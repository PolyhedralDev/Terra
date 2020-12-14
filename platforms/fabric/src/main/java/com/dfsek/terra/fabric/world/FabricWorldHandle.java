package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.generic.Tree;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.BlockArgumentParser;

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
            return new FabricBlockData(parser.parse(true).getBlockState());
        } catch(CommandSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public MaterialData createMaterialData(String data) {
        return new FabricMaterialData(createBlockData(data).getHandle().getMaterial());
    }

    @Override
    public Tree getTree(String id) {
        return null;
    }
}
