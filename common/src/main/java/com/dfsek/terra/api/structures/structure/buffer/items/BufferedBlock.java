package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;

public class BufferedBlock implements BufferedItem {
    private final BlockData data;
    private final boolean overwrite;
    private final TerraPlugin main;

    public BufferedBlock(BlockData data, boolean overwrite, TerraPlugin main) {
        this.data = data;
        this.overwrite = overwrite;
        this.main = main;
    }

    @Override
    public void paste(Location origin) {
        Block block = origin.getBlock();
        try {
            if(overwrite || block.isEmpty()) block.setBlockData(data, false);
        } catch(RuntimeException e) {
            main.logger().severe("Failed to place block at location " + origin + ": " + e.getMessage());
            main.getDebugLogger().stack(e);
        }
    }
}
