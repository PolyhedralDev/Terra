package com.dfsek.terra.addons.biome.pipeline.stages;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeExpander;
import com.dfsek.terra.addons.biome.pipeline.api.BiomeHolder;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;

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
    public BiomeHolder apply(BiomeHolder in, long seed) {
        return in.expand(expander, seed);
    }

    public enum Type {
        FRACTAL
    }
}
