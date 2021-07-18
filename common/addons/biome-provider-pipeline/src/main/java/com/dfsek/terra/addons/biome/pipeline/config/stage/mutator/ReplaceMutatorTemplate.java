package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.ReplaceMutator;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;

@SuppressWarnings("unused")
public class ReplaceMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private String from;

    @Value("to")
    private ProbabilityCollection<SeededTerraBiome> to;

    @Override
    public BiomeMutator build(long seed) {
        return new ReplaceMutator(from, to.map(biomeBuilder -> biomeBuilder.apply(seed), true), noise.apply(seed));
    }
}
