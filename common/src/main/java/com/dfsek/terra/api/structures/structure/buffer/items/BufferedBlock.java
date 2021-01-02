package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;

public class BufferedBlock implements BufferedItem {
    private final BlockData data;
    private final boolean overwrite;

    public BufferedBlock(BlockData data, boolean overwrite) {
        this.data = data;
        this.overwrite = overwrite;
    }

    @Override
    public void paste(Location origin) {
        Block block = origin.getBlock();
        if(overwrite || block.isEmpty()) block.setBlockData(data, false);
    }
}
