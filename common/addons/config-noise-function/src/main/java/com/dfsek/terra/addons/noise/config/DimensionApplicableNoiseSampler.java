/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


public class DimensionApplicableNoiseSampler implements ObjectTemplate<DimensionApplicableNoiseSampler> {
    @Value("dimensions")
    private @Meta int dimensions;
    
    @Value(".")
    private @Meta NoiseSampler sampler;
    
    @Override
    public DimensionApplicableNoiseSampler get() {
        return this;
    }
    
    public int getDimensions() {
        return dimensions;
    }
    
    public NoiseSampler getSampler() {
        return sampler;
    }
}
