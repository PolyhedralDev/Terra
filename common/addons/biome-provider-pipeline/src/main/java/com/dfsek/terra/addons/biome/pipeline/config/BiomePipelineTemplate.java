package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;

import java.util.List;

import com.dfsek.terra.addons.biome.pipeline.BiomePipeline;
import com.dfsek.terra.addons.biome.pipeline.BiomePipelineProvider;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class BiomePipelineTemplate extends BiomeProviderTemplate {
    private final TerraPlugin main;
    @Value("pipeline.initial-size")
    @Default
    private @Meta int initialSize = 2;
    
    @Value("pipeline.stages")
    private @Meta List<@Meta Stage> stages;
    
    @Value("pipeline.source")
    private @Meta BiomeSource source;
    
    public BiomePipelineTemplate(TerraPlugin main) {
        this.main = main;
    }
    
    @Override
    public BiomeProvider get() {
        BiomePipeline.BiomePipelineBuilder biomePipelineBuilder = new BiomePipeline.BiomePipelineBuilder(initialSize);
        stages.forEach(biomePipelineBuilder::addStage);
        BiomePipeline pipeline = biomePipelineBuilder.build(source);
        return new BiomePipelineProvider(pipeline, main, resolution, blend, blendAmp);
    }
}
