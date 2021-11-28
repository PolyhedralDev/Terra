/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.buffer.items;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.access.World;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BufferedStateManipulator implements BufferedItem {
    private final String data;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BufferedStateManipulator.class);
    
    public BufferedStateManipulator(String state) {
        this.data = state;
    }
    
    @Override
    public void paste(Vector3 origin, World world) {
        try {
            BlockEntity state = world.getBlockState(origin);
            state.applyState(data);
            state.update(false);
        } catch(Exception e) {
            LOGGER.warn("Could not apply BlockState at {}", origin, e);
            e.printStackTrace();
        }
    }
}
