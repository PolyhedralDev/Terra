package com.dfsek.terra.fabric.generation;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public interface BiomeProviderHolder {
    void setBiomeProvider(BiomeProvider biomeProvider);
    
    BiomeProvider getBiomeProvider();
}
