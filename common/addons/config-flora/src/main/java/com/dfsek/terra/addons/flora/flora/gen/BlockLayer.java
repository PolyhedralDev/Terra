/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.flora.flora.gen;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class BlockLayer {
    private final int layers;
    private final ProbabilityCollection<BlockState> blocks;
    
    public BlockLayer(int layers, ProbabilityCollection<BlockState> blocks) {
        this.layers = layers;
        this.blocks = blocks;
    }
    
    public int getLayers() {
        return layers;
    }
    
    public ProbabilityCollection<BlockState> getBlocks() {
        return blocks;
    }
}
