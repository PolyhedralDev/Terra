package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BorderMutator;
import com.dfsek.terra.config.builder.BiomeBuilder;

@SuppressWarnings("unused")
@AutoDocAlias("BorderMutator")
public class BorderMutatorTemplate extends MutatorStageTemplate {
    /**
     * Tag of the biome on the external side of the border.
     */
    @Value("from")
    private String from;

    /**
     * Tag of biomes to replace when bordering biomes
     * with tag "from"
     */
    @Value("replace")
    private String replace;

    /**
     * Collection of biomes to place at borders
     * of "from" and "to"
     */
    @Value("to")
    private ProbabilityCollection<BiomeBuilder> to;

    @Override
    public BiomeMutator build(long seed) {
        return new BorderMutator(from, replace, noise.apply(seed), to.map(biomeBuilder -> biomeBuilder.apply(seed), true));
    }
}
