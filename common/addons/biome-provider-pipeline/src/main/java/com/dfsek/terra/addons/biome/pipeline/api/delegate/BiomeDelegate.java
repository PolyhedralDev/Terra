package com.dfsek.terra.addons.biome.pipeline.api.delegate;

import com.dfsek.terra.api.util.StringIdentifiable;
import com.dfsek.terra.api.world.biome.Biome;

import java.util.Set;


public interface BiomeDelegate extends StringIdentifiable {
    Biome getBiome();
    
    Set<String> getTags();
    
    default boolean isEphemeral() {
        return false;
    }
    
    default boolean isSelf() {
        return false;
    }
    
    static BiomeDelegate ephemeral(String id) {
        return new EphemeralBiomeDelegate(id);
    }
    
    static BiomeDelegate from(Biome biome) {
        return new DelegatedBiome(biome);
    }
    
    static BiomeDelegate self() {
        return SelfDelegate.INSTANCE;
    }
    
    
}
