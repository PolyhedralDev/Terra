package com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator;

import com.dfsek.terra.api.world.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.api.world.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.api.world.biome.pipeline.stages.Stage;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.StageTemplate;

public abstract class MutatorStageTemplate extends StageTemplate {
    public abstract BiomeMutator build(long seed);

    @Override
    public Stage apply(Long seed) {
        return new MutatorStage(build(seed));
    }
}
