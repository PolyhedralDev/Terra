package com.dfsek.terra.api.world.biome.pipeline.stages;

import com.dfsek.terra.api.world.biome.pipeline.BiomeHolder;
import com.dfsek.terra.api.world.biome.pipeline.mutator.BiomeMutator;

public class MutatorStage implements Stage {
    private final BiomeMutator mutator;

    public MutatorStage(BiomeMutator mutator) {
        this.mutator = mutator;
    }

    @Override
    public boolean isExpansion() {
        return false;
    }

    @Override
    public BiomeHolder apply(BiomeHolder in) {
        in.mutate(mutator);
        return in;
    }

    public enum Type {
        REPLACE, REPLACE_LIST, BORDER, BORDER_LIST, SMOOTH
    }
}
