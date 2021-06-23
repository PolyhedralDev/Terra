package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.BlockFace;

public class BufferedPulledBlock implements BufferedItem {
    private final BlockData data;

    public BufferedPulledBlock(BlockData data) {
        this.data = data;
    }

    @Override
    public void paste(LocationImpl origin) {
        Block pos = origin.getBlock();
        while(pos.getY() > origin.getWorld().getMinHeight()) {
            if(!pos.isEmpty()) {
                pos.setBlockData(data, false);
                break;
            }
            pos = pos.getRelative(BlockFace.DOWN);
        }
    }
}
