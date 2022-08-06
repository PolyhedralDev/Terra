package com.dfsek.terra.addons.biome.pipeline.reimplementation.api.biome;

import java.util.Set;

import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.world.biome.Biome;


public interface PipelineBiome extends StringIdentifiable {
    Biome getBiome();
    
    static PipelineBiome ephemeral(String id) {
        return new EphemeralBiomeDelegate(id);
    }
    
    static PipelineBiome from(Biome biome) {
        return new DelegatedBiome(biome);
    }
    
    static PipelineBiome self() {
        return SelfDelegate.INSTANCE;
    }
    
    Set<String> getTags();
    
    default boolean isEphemeral() {
        return false;
    }
    
    default boolean isSelf() {
        return false;
    }
    
    
}