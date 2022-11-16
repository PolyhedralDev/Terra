package com.dfsek.terra.addons.biome.pipeline.api.delegate;

import java.util.Set;

import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.world.biome.Biome;


public interface BiomeDelegate extends StringIdentifiable {
    static BiomeDelegate ephemeral(String id) {
        return new EphemeralBiomeDelegate(id);
    }
    
    static BiomeDelegate from(Biome biome) {
        return new DelegatedBiome(biome);
    }
    
    static BiomeDelegate self() {
        return SelfDelegate.INSTANCE;
    }
    
    Biome getBiome();
    
    Set<String> getTags();
    
    default boolean isEphemeral() {
        return false;
    }
    
    default boolean isSelf() {
        return false;
    }
    
    
}
