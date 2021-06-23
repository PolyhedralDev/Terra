package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.util.collections.ProbabilityCollectionImpl;
import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeMutator;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BorderMutator;
import com.dfsek.terra.config.builder.BiomeBuilder;

@SuppressWarnings("unused")
public class BorderMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private String from;

    @Value("replace")
    private String replace;

    @Value("to")
    private ProbabilityCollectionImpl<BiomeBuilder> to;

    @Override
    public BiomeMutator build(long seed) {
        return new BorderMutator(from, replace, noise.apply(seed), to.map(biomeBuilder -> biomeBuilder.apply(seed), true));
    }
}
