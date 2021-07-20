package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.BorderListMutator;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.util.Map;

@SuppressWarnings("unused")
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
    public BiomeMutator getMutator() {
        return new BorderListMutator(replace, from, defaultReplace, noise, defaultTo);
    }
}
