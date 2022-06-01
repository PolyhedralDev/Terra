/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise.fractal;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.samplers.noise.fractal.FractalNoiseFunction;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


public abstract class FractalTemplate<T extends FractalNoiseFunction> extends SamplerTemplate<T> {
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
    protected @Meta NoiseSampler function;
}
