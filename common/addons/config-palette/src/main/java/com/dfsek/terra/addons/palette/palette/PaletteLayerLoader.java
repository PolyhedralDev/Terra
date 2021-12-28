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
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

import org.jetbrains.annotations.NotNull;


public class PaletteLayerLoader implements ObjectTemplate<PaletteLayerHolder> {
    @Value("materials")
    private ProbabilityCollection<BlockState> collection;
    
    @Value("sampler")
    @Default
    private NoiseSampler sampler = null;
    
    @Value("layers")
    private int layers;
    
    @Override
    public PaletteLayerHolder get() {
        return new PaletteLayerHolder(collection, sampler, layers);
    }
}
