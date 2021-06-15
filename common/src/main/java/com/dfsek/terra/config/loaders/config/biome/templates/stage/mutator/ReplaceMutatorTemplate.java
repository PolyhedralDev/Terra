package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.api.world.biome.pipeline.mutator.ReplaceMutator;
import com.dfsek.terra.config.builder.BiomeBuilder;

@SuppressWarnings("unused")
public class ReplaceMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private MetaValue<String> from;

    @Value("to")
    private ProbabilityCollection<MetaValue<BiomeBuilder>> to;

    @Override
    public BiomeMutator build(long seed) {
        return new ReplaceMutator(from.get(), to.map(MetaValue::get).map(biomeBuilder -> biomeBuilder.apply(seed)), noise.get().apply(seed));
    }
}
