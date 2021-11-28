/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.buffer.items;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.access.WorldAccess;


public class BufferedPulledBlock implements BufferedItem {
    private final BlockState data;
    
    public BufferedPulledBlock(BlockState data) {
        this.data = data;
    }
    
    @Override
    public void paste(Vector3 origin, WorldAccess world) {
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
