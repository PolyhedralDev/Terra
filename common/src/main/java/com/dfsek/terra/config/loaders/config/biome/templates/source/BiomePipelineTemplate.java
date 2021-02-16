package com.dfsek.terra.config.loaders.config.biome.templates.source;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.util.seeded.SourceSeeded;
import com.dfsek.terra.api.util.seeded.StageSeeded;
import com.dfsek.terra.biome.pipeline.BiomePipeline;
import com.dfsek.terra.biome.provider.BiomeProvider;
import com.dfsek.terra.biome.provider.StandardBiomeProvider;
import com.dfsek.terra.registry.config.BiomeRegistry;

import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class BiomePipelineTemplate extends BiomeProviderTemplate {
    private final TerraPlugin main;
    @Value("pipeline.initial-size")
    @Default
    private int initialSize = 2;

    @Value("pipeline.stages")
    private List<StageSeeded> stages;

    @Value("pipeline.source")
    private SourceSeeded source;

    public BiomePipelineTemplate(BiomeRegistry registry, TerraPlugin main) {
        super(registry);
        this.main = main;
    }

    @Override
    public BiomeProvider build(long seed) {
        BiomePipeline.BiomePipelineBuilder biomePipelineBuilder = new BiomePipeline.BiomePipelineBuilder(initialSize);
        stages.forEach(biomePipelineBuilder::addStage);
        BiomePipeline pipeline = biomePipelineBuilder.build(source.apply(seed), seed);
        return new StandardBiomeProvider(pipeline, main, resolution, blend.apply(seed), blendAmp, (int) seed);
    }
}
