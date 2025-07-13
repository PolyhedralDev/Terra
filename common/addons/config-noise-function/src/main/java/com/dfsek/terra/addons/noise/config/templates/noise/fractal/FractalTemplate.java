/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise.fractal;

import com.dfsek.seismic.algorithms.sampler.noise.fractal.FractalNoiseFunction;
import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.config.templates.noise.NoiseTemplate;
import com.dfsek.terra.api.config.meta.Meta;


public abstract class FractalTemplate<T extends FractalNoiseFunction> extends NoiseTemplate<T> {
    @Value("octaves")
    @Default
    protected @Meta int octaves = 3;

    @Value("gain")
    @Default
    protected @Meta double fractalGain = 0.5D;

    @Value("lacunarity")
    @Default
    protected @Meta double fractalLacunarity = 2.0D;

    @Value("weighted-strength")
    @Default
    protected @Meta double weightedStrength = 0.0D;

    @Value("sampler")
    protected @Meta Sampler function;
}
