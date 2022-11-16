/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.mutator;

import java.util.Objects;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeMutator;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.MathUtil;


public class SmoothMutator implements BiomeMutator {
    
    private final NoiseSampler sampler;
    
    public SmoothMutator(NoiseSampler sampler) {
        this.sampler = sampler;
    }
    
    @Override
    public BiomeDelegate mutate(ViewPoint viewPoint, double x, double z, long seed) {
        BiomeDelegate top = viewPoint.getBiome(1, 0);
        BiomeDelegate bottom = viewPoint.getBiome(-1, 0);
        BiomeDelegate left = viewPoint.getBiome(0, 1);
        BiomeDelegate right = viewPoint.getBiome(0, -1);
        
        
        boolean vert = Objects.equals(top, bottom) && top != null;
        boolean horiz = Objects.equals(left, right) && left != null;
        
        if(vert && horiz) {
            return MathUtil.normalizeIndex(sampler.noise(seed, x, z), 2) == 0 ? left : top;
        }
        
        if(vert) return top;
        if(horiz) return left;
        
        return viewPoint.getBiome(0, 0);
    }
}
