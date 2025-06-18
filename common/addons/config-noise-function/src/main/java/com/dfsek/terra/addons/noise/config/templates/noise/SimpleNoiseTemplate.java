/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.seismic.algorithms.sampler.noise.NoiseFunction;
import com.dfsek.seismic.type.sampler.Sampler;

import java.util.function.Supplier;


public class SimpleNoiseTemplate extends NoiseTemplate<NoiseFunction> {
    private final Supplier<NoiseFunction> samplerSupplier;

    public SimpleNoiseTemplate(Supplier<NoiseFunction> samplerSupplier) {
        this.samplerSupplier = samplerSupplier;
    }

    @Override
    public Sampler get() {
        NoiseFunction sampler = samplerSupplier.get();
        sampler.setFrequency(frequency);
        sampler.setSalt(salt);
        return sampler;
    }
}
