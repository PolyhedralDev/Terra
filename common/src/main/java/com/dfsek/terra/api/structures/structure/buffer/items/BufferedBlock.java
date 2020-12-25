package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.BlockData;

public class BufferedBlock implements BufferedItem {
    private final BlockData data;

    public BufferedBlock(BlockData data) {
        this.data = data;
    }

    @Override
    public void paste(Location origin) {
        origin.getBlock().setBlockData(data, false);
    }
}
