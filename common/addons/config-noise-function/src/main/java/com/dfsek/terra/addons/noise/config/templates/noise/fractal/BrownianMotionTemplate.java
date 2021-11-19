/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise.fractal;

import com.dfsek.terra.addons.noise.samplers.noise.fractal.BrownianMotionSampler;
import com.dfsek.terra.api.noise.NoiseSampler;


public class BrownianMotionTemplate extends FractalTemplate<BrownianMotionSampler> {
    @Override
    public NoiseSampler get() {
        BrownianMotionSampler sampler = new BrownianMotionSampler(function);
        sampler.setGain(fractalGain);
        sampler.setLacunarity(fractalLacunarity);
        sampler.setOctaves(octaves);
        sampler.setWeightedStrength(weightedStrength);
        return sampler;
    }
}
