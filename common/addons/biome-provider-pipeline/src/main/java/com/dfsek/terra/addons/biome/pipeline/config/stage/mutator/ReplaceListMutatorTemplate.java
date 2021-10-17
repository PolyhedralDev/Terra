package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.annotations.Value;

import java.util.Map;

import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.config.stage.StageTemplate;
import com.dfsek.terra.addons.biome.pipeline.mutator.ReplaceListMutator;
import com.dfsek.terra.addons.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;


@SuppressWarnings("unused")
public class ReplaceListMutatorTemplate extends StageTemplate {
    @Value("default-from")
    private @Meta String defaultFrom;
    
    @Value("default-to")
    private @Meta ProbabilityCollection<@Meta TerraBiome> defaultTo;
    
    @Value("to")
    private @Meta Map<@Meta TerraBiome, @Meta ProbabilityCollection<@Meta TerraBiome>> replace;
    
    @Override
    public Stage get() {
        return new MutatorStage(new ReplaceListMutator(replace, defaultFrom, defaultTo, noise));
    }
}
