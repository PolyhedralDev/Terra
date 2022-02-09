/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.dfsek.terra.addons.noise.samplers.noise.NoiseFunction;
import com.dfsek.terra.addons.noise.samplers.noise.NoiseFunction.Builder;
import com.dfsek.terra.api.noise.NoiseSampler;


public class SimpleNoiseTemplate extends NoiseTemplate<NoiseFunction> {
    private final BiFunction<Double, Long, ? extends NoiseSampler> constructor;
    
    public SimpleNoiseTemplate(BiFunction<Double, Long, ? extends NoiseSampler> constructor) {
        this.constructor = constructor;
    }
    
    @Override
    public NoiseSampler get() {
        return NoiseFunction.Builder.simple(constructor)
                .frequency(frequency)
                .salt(salt)
                .build();
    }
}
