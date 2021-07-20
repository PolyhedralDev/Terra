package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.ReplaceMutator;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;

@SuppressWarnings("unused")
public class ReplaceMutatorTemplate extends MutatorStageTemplate {
    @Value("from")
    private String from;

    @Value("to")
    private ProbabilityCollection<TerraBiome> to;

    @Override
    public BiomeMutator getMutator() {
        return new ReplaceMutator(from, to, noise);
    }
}
