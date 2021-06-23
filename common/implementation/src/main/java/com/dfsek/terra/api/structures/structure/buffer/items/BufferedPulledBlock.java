package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;

public class BufferedPulledBlock implements BufferedItem {
    private final BlockData data;

    public BufferedPulledBlock(BlockData data) {
        this.data = data;
    }

    @Override
    public void paste(Location origin) {
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
