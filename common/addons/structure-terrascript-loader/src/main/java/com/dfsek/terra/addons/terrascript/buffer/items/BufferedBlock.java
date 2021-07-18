package com.dfsek.terra.addons.terrascript.buffer.items;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.base.Properties;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;

public class BufferedBlock implements BufferedItem {
    private final BlockState data;
    private final boolean overwrite;
    private final TerraPlugin main;
    private final boolean waterlog;

    public BufferedBlock(BlockState data, boolean overwrite, TerraPlugin main, boolean waterlog) {
        this.data = data;
        this.overwrite = overwrite;
        this.main = main;
        this.waterlog = waterlog;
    }

    @Override
    public void paste(Vector3 origin, World world) {
        try {
            BlockState current = world.getBlockData(origin);
            if(overwrite || current.isAir()) {
                if(waterlog && current.has(Properties.WATERLOGGED) && current.getBlockType().isWater()) {
                    current.set(Properties.WATERLOGGED, true);
                }
                world.setBlockData(origin, data);
            }
        } catch(RuntimeException e) {
            main.logger().severe("Failed to place block at location " + origin + ": " + e.getMessage());
            main.getDebugLogger().stack(e);
        }
    }
}
