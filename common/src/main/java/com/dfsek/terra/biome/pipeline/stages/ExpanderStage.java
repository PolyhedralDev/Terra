package com.dfsek.terra.biome.pipeline.stages;

import com.dfsek.terra.biome.pipeline.BiomeHolder;
import com.dfsek.terra.biome.pipeline.expand.BiomeExpander;

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
}
