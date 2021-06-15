package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BorderMutator;
import com.dfsek.terra.config.builder.BiomeBuilder;

@SuppressWarnings("unused")
public class BorderMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private MetaValue<String> from;

    @Value("replace")
    private MetaValue<String> replace;

    @Value("to")
    private ProbabilityCollection<MetaValue<BiomeBuilder>> to;

    @Override
    public BiomeMutator build(long seed) {
        return new BorderMutator(from.get(), replace.get(), noise.get().apply(seed), to.map(MetaValue::get).map(biomeBuilder -> biomeBuilder.apply(seed)));
    }
}
