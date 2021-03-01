package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BorderListMutator;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.builder.UserDefinedBiomeBuilder;

import java.util.HashMap;
import java.util.Map;

public class BorderListMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private String from;

    @Value("default-replace")
    private String defaultReplace;

    @Value("default-to")
    private ProbabilityCollection<UserDefinedBiomeBuilder> defaultTo;

    @Value("replace")
    private Map<BiomeBuilder<? extends TerraBiome>, ProbabilityCollection<BiomeBuilder<? extends TerraBiome>>> replace;


    @Override
    public BiomeMutator build(long seed) {
        Map<TerraBiome, ProbabilityCollection<TerraBiome>> replaceMap = new HashMap<>();

        replace.forEach((biomeBuilder, biomeBuilders) -> replaceMap.put(biomeBuilder.apply(seed), biomeBuilders.map(builder -> builder.apply(seed), true)));

        return new BorderListMutator(replaceMap, from, defaultReplace, noise.apply(seed), defaultTo.map(biomeBuilder -> biomeBuilder.apply(seed), true));
    }
}
