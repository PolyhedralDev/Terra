/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Description;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.List;

import com.dfsek.terra.addons.biome.pipeline.BiomePipeline;
import com.dfsek.terra.addons.biome.pipeline.BiomePipelineProvider;
import com.dfsek.terra.addons.biome.pipeline.api.stage.Stage;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class BiomePipelineTemplate extends BiomeProviderTemplate {
    @Value("pipeline.final-size")
    @Default
    @Description("""
                 The minimum final size of biome chunks. This value must be at least 2. This may not be the exact final size.
                                  
                 It is recommended to keep biome chunks' final size in the range of [50, 300]
                 to prevent performance issues.""")
    private @Meta int finalSize = 50;
    
    @Value("pipeline.source")
    @Description("The Biome Source to use for initial population of biomes.")
    private @Meta BiomeSource source;
    
    @Value("pipeline.stages")
    @Description("A list of pipeline stages to apply to the result of #source")
    private @Meta List<@Meta Stage> stages;
    
    @Override
    public BiomeProvider get() {
        BiomePipeline.BiomePipelineBuilder biomePipelineBuilder = new BiomePipeline.BiomePipelineBuilder(finalSize);
        stages.forEach(biomePipelineBuilder::addStage);
        BiomePipeline pipeline = biomePipelineBuilder.build(source);
        return new BiomePipelineProvider(pipeline, resolution, blend, blendAmp);
    }
}
