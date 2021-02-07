package com.dfsek.terra.biome.pipeline.stages;

import com.dfsek.terra.biome.pipeline.BiomeHolder;
import com.dfsek.terra.biome.pipeline.mutator.BiomeMutator;

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
