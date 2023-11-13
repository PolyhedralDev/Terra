/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.v2.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Description;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.List;

import com.dfsek.terra.addons.biome.pipeline.v2.PipelineBiomeProvider;
import com.dfsek.terra.addons.biome.pipeline.v2.api.Source;
import com.dfsek.terra.addons.biome.pipeline.v2.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.v2.pipeline.PipelineImpl;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class BiomePipelineTemplate implements ObjectTemplate<BiomeProvider> {
    @Value("resolution")
    @Default
    @Description("""
                 The resolution at which to sample biomes.
                                  
                 Larger values are quadratically faster, but produce lower quality results.
                 For example, a value of 3 would sample every 3 blocks.""")
    protected @Meta int resolution = 1;
    @Value("blend.sampler")
    @Default
    @Description("A sampler to use for blending the edges of biomes via domain warping.")
    protected @Meta NoiseSampler blendSampler = NoiseSampler.zero();
    @Value("blend.amplitude")
    @Default
    @Description("The amplitude at which to perform blending.")
    protected @Meta double blendAmplitude = 0d;
    @Value("pipeline.source")
    @Description("The Biome Source to use for initial population of biomes.")
    private @Meta Source source;
    @Value("pipeline.stages")
    @Description("A list of pipeline stages to apply to the result of #source")
    private @Meta List<@Meta Stage> stages;

    @Override
    public BiomeProvider get() {
        return new PipelineBiomeProvider(new PipelineImpl(source, stages, resolution, 128), resolution, blendSampler, blendAmplitude);
    }
}
