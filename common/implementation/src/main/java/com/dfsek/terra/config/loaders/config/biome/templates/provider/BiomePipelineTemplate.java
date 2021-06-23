package com.dfsek.terra.config.loaders.config.biome.templates.provider;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.util.seeded.SourceSeeded;
import com.dfsek.terra.api.util.seeded.StageSeeded;
import com.dfsek.terra.api.world.biome.pipeline.BiomePipelineImpl;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.biome.provider.StandardBiomeProvider;

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

    public BiomePipelineTemplate(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public BiomeProvider build(long seed) {
        BiomePipelineImpl.BiomePipelineBuilder biomePipelineBuilder = new BiomePipelineImpl.BiomePipelineBuilder(initialSize);
        stages.forEach(biomePipelineBuilder::addStage);
        BiomePipelineImpl pipeline = biomePipelineBuilder.build(source.apply(seed), seed);
        return new StandardBiomeProvider(pipeline, main, resolution, blend.apply(seed), blendAmp, (int) seed);
    }
}
