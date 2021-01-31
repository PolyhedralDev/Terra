package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.biome.pipeline.mutator.ReplaceListMutator;

import java.util.Map;

@SuppressWarnings("unused")
public class ReplaceListMutatorTemplate extends MutatorStageTemplate {
    @Value("default-from")
    private String defaultFrom;

    @Value("default-to")
    private ProbabilityCollection<TerraBiome> defaultTo;

    @Value("to")
    private Map<TerraBiome, ProbabilityCollection<TerraBiome>> replace;

    @Override
    public BiomeMutator build(long seed) {
        return new ReplaceListMutator(replace, defaultFrom, defaultTo, noise.apply(seed));
    }
}
