package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.BiomePipeline;
import com.dfsek.terra.addons.biome.pipeline.BiomePipelineProvider;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeSource;

import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class BiomePipelineTemplate extends BiomeProviderTemplate {
    private final TerraPlugin main;
    @Value("pipeline.initial-size")
    @Default
    private int initialSize = 2;

    @Value("pipeline.stages")
    private List<Stage> stages;

    @Value("pipeline.source")
    private BiomeSource source;

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
