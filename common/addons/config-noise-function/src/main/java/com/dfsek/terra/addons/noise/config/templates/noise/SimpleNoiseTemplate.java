/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise;

import java.util.function.Supplier;

import com.dfsek.terra.addons.noise.samplers.noise.NoiseFunction;
import com.dfsek.terra.api.noise.NoiseSampler;


public class SimpleNoiseTemplate extends NoiseTemplate<NoiseFunction> {
    private final Supplier<NoiseFunction> samplerSupplier;
    
    public SimpleNoiseTemplate(Supplier<NoiseFunction> samplerSupplier) {
        this.samplerSupplier = samplerSupplier;
    }
    
    @Override
    public NoiseSampler get() {
        NoiseFunction sampler = samplerSupplier.get();
        sampler.setFrequency(frequency);
        sampler.setSalt(salt);
        return sampler;
    }
}
