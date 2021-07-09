package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.addons.biome.pipeline.config.stage.StageTemplate;

public abstract class MutatorStageTemplate extends StageTemplate {
    public abstract BiomeMutator build(long seed);

    @Override
    public Stage apply(Long seed) {
        return new MutatorStage(build(seed));
    }
}
