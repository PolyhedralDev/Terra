package com.dfsek.terra.config.loaders.config.biome.templates.provider;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.util.seeded.SourceSeeded;
import com.dfsek.terra.api.util.seeded.StageSeeded;
import com.dfsek.terra.api.world.biome.pipeline.BiomePipeline;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.biome.provider.StandardBiomeProvider;

import java.util.List;

/**
 * Configures a biome pipeline.
 */
@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class BiomePipelineTemplate extends BiomeProviderTemplate {
    private final TerraPlugin main;
    /**
     * Initial size of biome pipeline chunks.
     */
    @Value("pipeline.initial-size")
    @Default
    private int initialSize = 2;

    /**
     * Mutator stages to be used in this biome pipeline.
     */
    @Value("pipeline.stages")
    private List<StageSeeded> stages;

    /**
     * Biome source to initialize the pipeline.
     */
    @Value("pipeline.source")
    private SourceSeeded source;

    public BiomePipelineTemplate(TerraPlugin main) {
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
