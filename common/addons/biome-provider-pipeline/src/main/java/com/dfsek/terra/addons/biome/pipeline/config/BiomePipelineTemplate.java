package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.BiomePipelineImpl;
import com.dfsek.terra.addons.biome.pipeline.StageSeeded;
import com.dfsek.terra.addons.biome.pipeline.StandardBiomeProvider;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.util.seeded.SeededBiomeSource;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;

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
    private SeededBiomeSource source;

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
