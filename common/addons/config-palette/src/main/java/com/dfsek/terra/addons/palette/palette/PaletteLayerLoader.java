/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.palette.palette;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class PaletteLayerLoader implements ObjectTemplate<PaletteLayerHolder> {
    @Value("materials")
    private @Meta ProbabilityCollection<@Meta BlockState> collection;
    
    @Value("sampler")
    @Default
    private @Meta NoiseSampler sampler = null;
    
    @Value("layers")
    private @Meta int layers;
    
    @Override
    public PaletteLayerHolder get() {
        return new PaletteLayerHolder(collection, sampler, layers);
    }
}
