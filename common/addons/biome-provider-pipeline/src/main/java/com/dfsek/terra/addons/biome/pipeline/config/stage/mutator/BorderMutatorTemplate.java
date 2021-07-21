package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.BorderMutator;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;

@SuppressWarnings("unused")
public class BorderMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private @Meta String from;

    @Value("replace")
    private @Meta String replace;

    @Value("to")
    private @Meta ProbabilityCollection<@Meta TerraBiome> to;

    @Override
    public BiomeMutator get() {
        return new BorderMutator(from, replace, noise, to);
    }
}
