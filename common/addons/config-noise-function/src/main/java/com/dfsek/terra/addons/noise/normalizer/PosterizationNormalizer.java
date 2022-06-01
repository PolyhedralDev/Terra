/*
 * Copyright (c) 2022 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.normalizer;

import net.jafama.FastMath;

import com.dfsek.terra.api.noise.NoiseSampler;


public class PosterizationNormalizer extends Normalizer {
    private final double stepSize;
    
    public PosterizationNormalizer(NoiseSampler sampler, int steps) {
        super(sampler);
        this.stepSize = 2.0 / (steps - 1);
    }
    
    @Override
    public double normalize(double in) {
        return FastMath.roundToInt((in + 1) / stepSize) * stepSize - 1;
    }
}
