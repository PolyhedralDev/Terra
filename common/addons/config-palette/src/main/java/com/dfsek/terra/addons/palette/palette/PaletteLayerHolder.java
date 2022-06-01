/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.palette.palette;

import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class PaletteLayerHolder {
    private final ProbabilityCollection<BlockState> layer;
    private final NoiseSampler sampler;
    private final int size;
    
    public PaletteLayerHolder(@NotNull ProbabilityCollection<BlockState> layer, NoiseSampler sampler, int size) {
        this.layer = layer;
        this.sampler = sampler;
        this.size = size;
    }
    
    @NotNull
    public ProbabilityCollection<BlockState> getLayer() {
        return layer;
    }
    
    public int getSize() {
        return size;
    }
    
    public NoiseSampler getSampler() {
        return sampler;
    }
}
