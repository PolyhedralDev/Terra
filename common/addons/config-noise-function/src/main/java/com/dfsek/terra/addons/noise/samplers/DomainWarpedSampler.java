/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.samplers;

import com.dfsek.terra.api.noise.NoiseSampler;


public class DomainWarpedSampler implements NoiseSampler {
    private final NoiseSampler function;
    private final NoiseSampler warp;
    private final double amplitude;
    
    public DomainWarpedSampler(NoiseSampler function, NoiseSampler warp, double amplitude) {
        this.function = function;
        this.warp = warp;
        this.amplitude = amplitude;
    }
    
    @Override
    public double noise(long seed, double x, double y) {
        return function.noise(seed++,
                              x + warp.noise(seed++, x, y) * amplitude,
                              y + warp.noise(seed, x, y) * amplitude
                             );
    }
    
    @Override
    public double noise(long seed, double x, double y, double z) {
        return function.noise(seed++,
                              x + warp.noise(seed++, x, y, z) * amplitude,
                              y + warp.noise(seed++, x, y, z) * amplitude,
                              z + warp.noise(seed, x, y, z) * amplitude
                             );
    }
}
