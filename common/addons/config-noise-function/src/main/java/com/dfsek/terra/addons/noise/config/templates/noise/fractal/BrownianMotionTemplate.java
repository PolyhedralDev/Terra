/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise.fractal;


import com.dfsek.seismic.algorithms.sampler.noise.fractal.BrownianMotionSampler;
import com.dfsek.seismic.type.sampler.Sampler;


public class BrownianMotionTemplate extends FractalTemplate<BrownianMotionSampler> {
    @Override
    public Sampler get() {
        BrownianMotionSampler sampler = new BrownianMotionSampler(salt, function, fractalGain, fractalLacunarity, weightedStrength, octaves);
        return sampler;
    }
}
