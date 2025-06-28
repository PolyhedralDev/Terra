/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.seismic.algorithms.sampler.noise.NoiseFunction;
import com.dfsek.seismic.type.sampler.Sampler;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;


public class SimpleNoiseTemplate<T extends NoiseFunction> extends NoiseTemplate<NoiseFunction> {
    private final Class<T> samplerClass;

    public SimpleNoiseTemplate(Class<T> samplerClass) {
        this.samplerClass = samplerClass;
    }

    @Override
    public Sampler get() {
        NoiseFunction sampler = null;
        try {
            sampler = samplerClass.getConstructor(double.class, long.class)
                    .newInstance(frequency, salt);
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return sampler;
    }
}
