package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.vector.Vector3;

public class BufferedPulledBlock implements BufferedItem {
    private final BlockData data;

    public BufferedPulledBlock(BlockData data) {
        this.data = data;
    }

    @Override
    public void paste(Location origin) {
        Vector3 mutable = origin.toVector();
        while(mutable.getY() > origin.getWorld().getMinHeight()) {
            if(!origin.getWorld().getBlockData(mutable).isAir()) {
                origin.getWorld().setBlockData(mutable, data);
                break;
            }
            mutable.subtract(0, 1, 0);
        }
    }
}
