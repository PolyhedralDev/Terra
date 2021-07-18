package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.BorderListMutator;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class BorderListMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private String from;

    @Value("default-replace")
    private String defaultReplace;

    @Value("default-to")
    private ProbabilityCollection<SeededTerraBiome> defaultTo;

    @Value("replace")
    private Map<SeededTerraBiome, ProbabilityCollection<SeededTerraBiome>> replace;


    @Override
    public BiomeMutator getMutator(long seed) {
        Map<TerraBiome, ProbabilityCollection<TerraBiome>> replaceMap = new HashMap<>();

        replace.forEach((keyBuilder, replacements) -> replaceMap.put(keyBuilder.build(seed), replacements.map(replacement -> replacement.build(seed), true)));

        return new BorderListMutator(replaceMap, from, defaultReplace, noise.build(seed), defaultTo.map(biomeBuilder -> biomeBuilder.build(seed), true));
    }
}
