/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;

import java.util.List;

import com.dfsek.terra.addons.biome.pipeline.BiomePipeline;
import com.dfsek.terra.addons.biome.pipeline.BiomePipelineProvider;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class BiomePipelineTemplate extends BiomeProviderTemplate {
    private final Platform platform;
    @Value("pipeline.initial-size")
    @Default
    private @Meta int initialSize = 2;
    
    @Value("pipeline.stages")
    private @Meta List<@Meta Stage> stages;
    
    @Value("pipeline.source")
    private @Meta BiomeSource source;
    
    public BiomePipelineTemplate(Platform platform) {
        this.platform = platform;
    }
    
    @Override
    public BiomeProvider get() {
        BiomePipeline.BiomePipelineBuilder biomePipelineBuilder = new BiomePipeline.BiomePipelineBuilder(initialSize);
        stages.forEach(biomePipelineBuilder::addStage);
        BiomePipeline pipeline = biomePipelineBuilder.build(source);
        return new BiomePipelineProvider(pipeline, platform, resolution, blend, blendAmp);
    }
}
