/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.source;

import com.dfsek.seismic.type.sampler.Sampler;

import com.dfsek.terra.addons.biome.pipeline.api.Source;
import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class SamplerSource implements Source {
    private final ProbabilityCollection<PipelineBiome> biomes;
    private final Sampler sampler;

    public SamplerSource(ProbabilityCollection<PipelineBiome> biomes, Sampler sampler) {
        this.biomes = biomes;
        this.sampler = sampler;
    }

    @Override
    public PipelineBiome get(long seed, int x, int z) {
        return biomes.get(sampler, x, z, seed);
    }

    @Override
    public Iterable<PipelineBiome> getBiomes() {
        return biomes.getContents();
    }
}
