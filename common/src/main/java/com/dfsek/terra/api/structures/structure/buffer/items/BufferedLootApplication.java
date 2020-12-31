package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.BlockData;

public class BufferedLootApplication implements BufferedItem {
    @Override
    public void paste(Location origin) {
        BlockData data = origin.getBlock().getBlockData();
    }
}
