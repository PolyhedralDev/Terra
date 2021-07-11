package com.dfsek.terra.addons.biome.pipeline.api;

public interface Stage {
    boolean isExpansion();

    BiomeHolder apply(BiomeHolder in);

}
