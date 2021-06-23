package com.dfsek.terra.api.world.biome.pipeline.stages;

import com.dfsek.terra.api.world.biome.pipeline.BiomeHolder;
import com.dfsek.terra.api.world.biome.pipeline.expand.BiomeExpander;

public class ExpanderStage implements Stage {
    private final BiomeExpander expander;

    public ExpanderStage(BiomeExpander expander) {
        this.expander = expander;
    }

    @Override
    public boolean isExpansion() {
        return true;
    }

    @Override
    public BiomeHolder apply(BiomeHolder in) {
        return in.expand(expander);
    }

    public enum Type {
        FRACTAL
    }
}
