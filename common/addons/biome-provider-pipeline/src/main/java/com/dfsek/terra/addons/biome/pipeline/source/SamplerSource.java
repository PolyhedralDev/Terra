/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.source;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class SamplerSource implements BiomeSource {
    private final ProbabilityCollection<BiomeDelegate> biomes;
    private final NoiseSampler sampler;
    
    public SamplerSource(ProbabilityCollection<BiomeDelegate> biomes, NoiseSampler sampler) {
        this.biomes = biomes;
        this.sampler = sampler;
    }
    
    @Override
    public BiomeDelegate getBiome(double x, double z, long seed) {
        return biomes.get(sampler, x, z, seed);
    }
    
    @Override
    public Iterable<BiomeDelegate> getBiomes() {
        return biomes.getContents();
    }
}
