package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.ReplaceListMutator;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.util.Map;

@SuppressWarnings("unused")
public class ReplaceListMutatorTemplate extends MutatorStageTemplate {
    @Value("default-from")
    private @Meta String defaultFrom;

    @Value("default-to")
    private @Meta ProbabilityCollection<@Meta TerraBiome> defaultTo;

    @Value("to")
    private @Meta Map<@Meta TerraBiome, @Meta ProbabilityCollection<@Meta TerraBiome>> replace;

    @Override
    public BiomeMutator get() {
        return new ReplaceListMutator(replace, defaultFrom, defaultTo, noise);
    }
}
