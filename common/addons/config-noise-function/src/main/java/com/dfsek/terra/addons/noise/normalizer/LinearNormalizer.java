/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.normalizer;

import com.dfsek.terra.api.noise.NoiseSampler;


/**
 * Normalizer to linearly scale data's range.
 */
public class LinearNormalizer extends Normalizer {
    private final double min;
    private final double max;
    
    public LinearNormalizer(NoiseSampler sampler, double min, double max) {
        super(sampler);
        this.min = min;
        this.max = max;
    }
    
    @Override
    public double normalize(double in) {
        return (in - min) * (2 / (max - min)) - 1;
    }
}
