package com.dfsek.terra.config.loaders.config.biome.templates.provider;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.util.seeded.SourceSeeded;
import com.dfsek.terra.api.util.seeded.StageSeeded;
import com.dfsek.terra.api.world.biome.pipeline.BiomePipeline;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.biome.provider.StandardBiomeProvider;

import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class BiomePipelineTemplate extends BiomeProviderTemplate {
    private final TerraPlugin main;
    @Value("pipeline.initial-size")
    @Default
    private MetaValue<Integer> initialSize = MetaValue.of(2);

    @Value("pipeline.stages")
    private List<MetaValue<StageSeeded>> stages;

    @Value("pipeline.source")
    private MetaValue<SourceSeeded> source;

    public BiomePipelineTemplate(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public BiomeProvider build(long seed) {
        BiomePipeline.BiomePipelineBuilder biomePipelineBuilder = new BiomePipeline.BiomePipelineBuilder(initialSize.get());
        stages.stream().map(MetaValue::get).forEach(biomePipelineBuilder::addStage);
        BiomePipeline pipeline = biomePipelineBuilder.build(source.get().apply(seed), seed);
        return new StandardBiomeProvider(pipeline, main, resolution.get(), blend.get().apply(seed), blendAmp.get(), (int) seed);
    }
}
