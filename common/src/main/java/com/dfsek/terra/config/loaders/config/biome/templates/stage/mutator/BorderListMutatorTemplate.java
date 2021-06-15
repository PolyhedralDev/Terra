package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BorderListMutator;
import com.dfsek.terra.config.builder.BiomeBuilder;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class BorderListMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private MetaValue<String> from;

    @Value("default-replace")
    private MetaValue<String> defaultReplace;

    @Value("default-to")
    private ProbabilityCollection<MetaValue<BiomeBuilder>> defaultTo;

    @Value("replace")
    private Map<BiomeBuilder, ProbabilityCollection<MetaValue<BiomeBuilder>>> replace;


    @Override
    public BiomeMutator build(long seed) {
        Map<TerraBiome, ProbabilityCollection<TerraBiome>> replaceMap = new HashMap<>();

        replace.forEach((keyBuilder, replacements) -> replaceMap.put(keyBuilder.apply(seed), replacements.map(replacement -> replacement.get().apply(seed), true)));

        return new BorderListMutator(replaceMap, from.get(), defaultReplace.get(), noise.get().apply(seed), defaultTo.map(biomeBuilder -> biomeBuilder.get().apply(seed), true));
    }
}
