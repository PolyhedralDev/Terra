package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.BorderListMutator;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;
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
    private ProbabilityCollection<BiomeBuilder> defaultTo;

    @Value("replace")
    private Map<BiomeBuilder, ProbabilityCollection<BiomeBuilder>> replace;


    @Override
    public BiomeMutator build(long seed) {
        Map<TerraBiome, ProbabilityCollection<TerraBiome>> replaceMap = new HashMap<>();

        replace.forEach((keyBuilder, replacements) -> replaceMap.put(keyBuilder.apply(seed), replacements.map(replacement -> replacement.apply(seed), true)));

        return new BorderListMutator(replaceMap, from, defaultReplace, noise.apply(seed), defaultTo.map(biomeBuilder -> biomeBuilder.apply(seed), true));
    }
}
