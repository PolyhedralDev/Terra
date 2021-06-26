package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;

public class BufferedPulledBlock implements BufferedItem {
    private final BlockState data;

    public BufferedPulledBlock(BlockState data) {
        this.data = data;
    }

    @Override
    public void paste(Vector3 origin, World world) {
        Vector3 mutable = origin.clone();
        while(mutable.getY() > world.getMinHeight()) {
            if(!world.getBlockData(mutable).isAir()) {
                world.setBlockData(mutable, data);
                break;
            }
            mutable.subtract(0, 1, 0);
        }
    }
}
