package com.dfsek.terra.fabric.mixin_ifaces;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public interface BiomeProviderHolder {
    void terra$setHeldBiomeProvider(BiomeProvider biomeProvider);
    
    BiomeProvider terra$getHeldBiomeProvider();
}
