package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.config.stage.StageTemplate;
import com.dfsek.terra.addons.biome.pipeline.mutator.SmoothMutator;
import com.dfsek.terra.addons.biome.pipeline.stages.MutatorStage;

public class SmoothMutatorTemplate extends StageTemplate {
    @Override
    public Stage get() {
        return new MutatorStage(new SmoothMutator(noise));
    }
}
