package com.dfsek.terra.api.world.biome.generation.pipeline;

import com.dfsek.terra.api.world.biome.generation.pipeline.BiomeHolder;

public interface Stage {
    boolean isExpansion();

    BiomeHolder apply(BiomeHolder in);

}
