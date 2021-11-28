/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.buffer.items;

import org.jetbrains.annotations.ApiStatus.Experimental;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.base.Properties;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.access.World;

@Experimental
public class BufferedBlock implements BufferedItem {
    private static final Logger logger = LoggerFactory.getLogger(BufferedBlock.class);
    
    private final BlockState data;
    private final boolean overwrite;
    private final Platform platform;
    private final boolean waterlog;
    
    public BufferedBlock(BlockState data, boolean overwrite, Platform platform, boolean waterlog) {
        this.data = data;
        this.overwrite = overwrite;
        this.platform = platform;
        this.waterlog = waterlog;
    }
    
    @Override
    public void paste(Vector3 origin, World world) {
        try {
            BlockState current = world.getBlockData(origin);
            if(overwrite || current.isAir()) {
                if(waterlog && current.has(Properties.WATERLOGGED) && current.getBlockType().isWater()) {
                    current.set(Properties.WATERLOGGED, true);
                }
                world.setBlockData(origin, data);
            }
        } catch(RuntimeException e) {
            logger.error("Failed to place block at location {}", origin, e);
        }
    }
}
