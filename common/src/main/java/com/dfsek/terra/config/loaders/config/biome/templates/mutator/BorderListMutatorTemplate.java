package com.dfsek.terra.config.loaders.config.biome.templates.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.biome.pipeline.mutator.BorderListMutator;

import java.util.Map;

public class BorderListMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private String from;

    @Value("default-replace")
    private String defaultReplace;

    @Value("default-to")
    private ProbabilityCollection<TerraBiome> defaultTo;

    @Value("replace")
    private Map<TerraBiome, ProbabilityCollection<TerraBiome>> replace;


    @Override
    public BiomeMutator build(long seed) {
        return new BorderListMutator(replace, from, defaultReplace, noise.apply(seed), defaultTo);
    }
}
