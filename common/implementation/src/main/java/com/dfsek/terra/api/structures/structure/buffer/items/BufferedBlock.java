package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.data.Waterlogged;

public class BufferedBlock implements BufferedItem {
    private final BlockData data;
    private final boolean overwrite;
    private final TerraPlugin main;
    private final boolean waterlog;

    public BufferedBlock(BlockData data, boolean overwrite, TerraPlugin main, boolean waterlog) {
        this.data = data;
        this.overwrite = overwrite;
        this.main = main;
        this.waterlog = waterlog;
    }

    @Override
    public void paste(Location origin) {
        Block block = origin.getBlock();
        try {
            if(overwrite || block.isEmpty()) {
                if(waterlog && data instanceof Waterlogged && block.getBlockData().getBlockType().isWater())
                    ((Waterlogged) data).setWaterlogged(true);
                block.setBlockData(data, false);
            }
        } catch(RuntimeException e) {
            main.logger().severe("Failed to place block at location " + origin + ": " + e.getMessage());
            main.getDebugLogger().stack(e);
        }
    }
}
