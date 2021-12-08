/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.expand;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeExpander;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.MathUtil;


public class FractalExpander implements BiomeExpander {
    private final NoiseSampler sampler;
    
    public FractalExpander(NoiseSampler sampler) {
        this.sampler = sampler;
    }
    
    @Override
    public BiomeDelegate getBetween(double x, double z, long seed, BiomeDelegate... others) {
        return others[MathUtil.normalizeIndex(sampler.noise(seed, x, z), others.length)];
    }
}
