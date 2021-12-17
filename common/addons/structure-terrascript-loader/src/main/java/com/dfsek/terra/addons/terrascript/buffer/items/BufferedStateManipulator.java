/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.buffer.items;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.WritableWorld;


public class BufferedStateManipulator implements BufferedItem {
    private static final Logger LOGGER = LoggerFactory.getLogger(BufferedStateManipulator.class);
    private final String data;
    
    public BufferedStateManipulator(String state) {
        this.data = state;
    }
    
    @Override
    public void paste(Vector3 origin, WritableWorld world) {
        try {
            BlockEntity state = world.getBlockEntity(origin);
            state.applyState(data);
            state.update(false);
        } catch(Exception e) {
            LOGGER.warn("Could not apply BlockState at {}", origin, e);
            e.printStackTrace();
        }
    }
}
