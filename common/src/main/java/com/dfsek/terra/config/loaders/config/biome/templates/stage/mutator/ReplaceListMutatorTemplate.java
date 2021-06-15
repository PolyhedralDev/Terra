package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.api.world.biome.pipeline.mutator.ReplaceListMutator;
import com.dfsek.terra.config.builder.BiomeBuilder;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ReplaceListMutatorTemplate extends MutatorStageTemplate {
    @Value("default-from")
    private MetaValue<String> defaultFrom;

    @Value("default-to")
    private ProbabilityCollection<MetaValue<BiomeBuilder>> defaultTo;

    @Value("to")
    private Map<BiomeBuilder, ProbabilityCollection<MetaValue<BiomeBuilder>>> replace;

    @Override
    public BiomeMutator build(long seed) {
        Map<TerraBiome, ProbabilityCollection<TerraBiome>> replaceMap = new HashMap<>();

        replace.forEach((biomeBuilder, biomeBuilders) -> replaceMap.put(biomeBuilder.apply(seed), biomeBuilders.map(builder -> builder.get().apply(seed), true)));

        return new ReplaceListMutator(replaceMap, defaultFrom.get(), defaultTo.map(biomeBuilder -> biomeBuilder.get().apply(seed), true), noise.get().apply(seed));
    }
}
