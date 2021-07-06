package com.dfsek.terra.api.world.biome.generation.pipeline;

public interface Stage {
    boolean isExpansion();

    BiomeHolder apply(BiomeHolder in);

}
