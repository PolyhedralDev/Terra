package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.api.world.biome.pipeline.mutator.ReplaceMutator;
import com.dfsek.terra.config.builder.BiomeBuilder;

@SuppressWarnings("unused")
@AutoDocAlias("ReplaceMutator")
public class ReplaceMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private String from;

    @Value("to")
    private ProbabilityCollection<BiomeBuilder> to;

    @Override
    public BiomeMutator build(long seed) {
        return new ReplaceMutator(from, to.map(biomeBuilder -> biomeBuilder.apply(seed), true), noise.apply(seed));
    }
}
