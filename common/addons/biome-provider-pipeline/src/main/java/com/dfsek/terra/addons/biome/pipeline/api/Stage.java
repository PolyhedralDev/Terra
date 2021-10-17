package com.dfsek.terra.addons.biome.pipeline.api;

public interface Stage {
    BiomeHolder apply(BiomeHolder in, long seed);
    
    boolean isExpansion();
    
}
