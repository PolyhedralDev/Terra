package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.biome.pipeline.mutator.ReplaceMutator;

@SuppressWarnings("unused")
public class ReplaceMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private String from;

    @Value("to")
    private ProbabilityCollection<TerraBiome> to;

    @Override
    public BiomeMutator build(long seed) {
        return new ReplaceMutator(from, to, noise.apply(seed));
    }
}
