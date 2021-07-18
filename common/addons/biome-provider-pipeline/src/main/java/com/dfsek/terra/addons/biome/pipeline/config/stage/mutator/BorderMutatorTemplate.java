package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.BorderMutator;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;

@SuppressWarnings("unused")
public class BorderMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private String from;

    @Value("replace")
    private String replace;

    @Value("to")
    private ProbabilityCollection<SeededTerraBiome> to;

    @Override
    public BiomeMutator getMutator(long seed) {
        return new BorderMutator(from, replace, noise.build(seed), to.map(biomeBuilder -> biomeBuilder.build(seed), true));
    }
}
