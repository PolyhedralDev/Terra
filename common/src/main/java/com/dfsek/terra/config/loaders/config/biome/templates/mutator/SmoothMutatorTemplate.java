package com.dfsek.terra.config.loaders.config.biome.templates.mutator;

import com.dfsek.terra.biome.pipeline.mutator.BiomeMutator;
import com.dfsek.terra.biome.pipeline.mutator.SmoothMutator;

public class SmoothMutatorTemplate extends MutatorStageTemplate {
    @Override
    public BiomeMutator build(long seed) {
        return new SmoothMutator(noise.apply(seed));
    }
}
