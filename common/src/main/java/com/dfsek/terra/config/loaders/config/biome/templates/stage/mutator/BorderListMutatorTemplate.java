package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BorderListMutator;
import com.dfsek.terra.config.builder.BiomeBuilder;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@AutoDocAlias("BorderListMutator")
public class BorderListMutatorTemplate extends MutatorStageTemplate {
    /**
     * Tag of the biome on the external side of the border.
     */
    @Value("from")
    private String from;

    /**
     * Tag of biomes to replace when bordering biomes with
     * tag "from".
     */
    @Value("default-replace")
    private String defaultReplace;

    /**
     * Default replacement biomes.
     */
    @Value("default-to")
    private ProbabilityCollection<BiomeBuilder> defaultTo;

    /**
     * Map of single biomes to their replacements.
     */
    @Value("replace")
    private Map<BiomeBuilder, ProbabilityCollection<BiomeBuilder>> replace;


    @Override
    public BiomeMutator build(long seed) {
        Map<TerraBiome, ProbabilityCollection<TerraBiome>> replaceMap = new HashMap<>();

        replace.forEach((keyBuilder, replacements) -> replaceMap.put(keyBuilder.apply(seed), replacements.map(replacement -> replacement.apply(seed), true)));

        return new BorderListMutator(replaceMap, from, defaultReplace, noise.apply(seed), defaultTo.map(biomeBuilder -> biomeBuilder.apply(seed), true));
    }
}
