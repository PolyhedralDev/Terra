/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.api.config.template.annotations.Description;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;
import com.dfsek.terra.addons.biome.pipeline.source.SamplerSource;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class SamplerSourceTemplate extends SourceTemplate {
    @Value("sampler")
    @Description("The sampler used to distribute biomes.")
    private @Meta NoiseSampler noise;
    
    @Value("biomes")
    @Description("The biomes to be distributed.")
    private @Meta ProbabilityCollection<@Meta BiomeDelegate> biomes;
    
    @Override
    public BiomeSource get() {
        return new SamplerSource(biomes, noise);
    }
}
