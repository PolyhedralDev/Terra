/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.v2.config.source;

import com.dfsek.tectonic.api.config.template.annotations.Description;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.biome.pipeline.v2.api.Source;
import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.v2.source.SamplerSource;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class SamplerSourceTemplate extends SourceTemplate {
    @Value("sampler")
    @Description("The sampler used to distribute biomes.")
    private @Meta NoiseSampler noise;

    @Value("biomes")
    @Description("The biomes to be distributed.")
    private @Meta ProbabilityCollection<@Meta PipelineBiome> biomes;

    @Override
    public Source get() {
        return new SamplerSource(biomes, noise);
    }
}
