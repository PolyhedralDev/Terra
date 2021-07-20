package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.SmoothMutator;

public class SmoothMutatorTemplate extends MutatorStageTemplate {
    @Override
    public BiomeMutator get() {
        return new SmoothMutator(noise);
    }
}
