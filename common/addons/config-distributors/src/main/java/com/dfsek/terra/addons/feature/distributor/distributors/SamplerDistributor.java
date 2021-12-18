/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.distributor.distributors;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Distributor;


public class SamplerDistributor implements Distributor {
    private final NoiseSampler sampler;
    
    private final double threshold;
    
    public SamplerDistributor(NoiseSampler sampler, double threshold) {
        this.sampler = sampler;
        this.threshold = threshold;
    }
    
    @Override
    public boolean matches(int x, int z, long seed) {
        return sampler.noise(seed, x, z) < threshold;
    }
}
