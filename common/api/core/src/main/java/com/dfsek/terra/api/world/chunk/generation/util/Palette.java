/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.chunk.generation.util;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public interface Palette {
    Palette add(BlockState m, int layers, NoiseSampler sampler);
    
    Palette add(ProbabilityCollection<BlockState> m, int layers, NoiseSampler sampler);
    
    /**
     * Fetches a material from the palette, at a given layer.
     *
     * @param layer - The layer at which to fetch the material.
     *
     * @return BlockData - The material fetched.
     */
    BlockState get(int layer, double x, double y, double z, long seed);
    
    int getSize();
}
