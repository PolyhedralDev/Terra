package com.dfsek.terra.biome.pipeline.stages;

import com.dfsek.terra.biome.pipeline.BiomeHolder;

public interface Stage {
    boolean isExpansion();

    BiomeHolder apply(BiomeHolder in);
}
