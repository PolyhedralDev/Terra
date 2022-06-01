/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise.fractal;

import com.dfsek.terra.addons.noise.samplers.noise.fractal.RidgedFractalSampler;
import com.dfsek.terra.api.noise.NoiseSampler;


public class RidgedFractalTemplate extends FractalTemplate<RidgedFractalSampler> {
    @Override
    public NoiseSampler get() {
        RidgedFractalSampler sampler = new RidgedFractalSampler(function);
        sampler.setGain(fractalGain);
        sampler.setLacunarity(fractalLacunarity);
        sampler.setOctaves(octaves);
        sampler.setWeightedStrength(weightedStrength);
        return sampler;
    }
}
