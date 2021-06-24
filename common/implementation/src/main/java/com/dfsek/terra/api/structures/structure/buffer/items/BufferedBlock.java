package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.data.Waterlogged;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.vector.Location;

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
        try {
            BlockData data = origin.getWorld().getBlockData(origin.toVector());
            if(overwrite || data.isAir()) {
                if(waterlog && data instanceof Waterlogged && data.getBlockType().isWater())
                    ((Waterlogged) data).setWaterlogged(true);
                origin.getWorld().setBlockData(origin.getVector(), data);
            }
        } catch(RuntimeException e) {
            main.logger().severe("Failed to place block at location " + origin + ": " + e.getMessage());
            main.getDebugLogger().stack(e);
        }
    }
}
