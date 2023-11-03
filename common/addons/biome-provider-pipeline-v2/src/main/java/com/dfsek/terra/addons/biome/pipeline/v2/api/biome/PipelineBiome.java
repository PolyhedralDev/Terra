package com.dfsek.terra.addons.biome.pipeline.v2.api.biome;

import java.util.Set;

import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.world.biome.Biome;


public interface PipelineBiome extends StringIdentifiable {
    static PipelineBiome placeholder(String id) {
        return new PlaceholderPipelineBiome(id);
    }
    
    static PipelineBiome from(Biome biome) {
        return new DelegatedPipelineBiome(biome);
    }
    
    static PipelineBiome self() {
        return SelfPipelineBiome.INSTANCE;
    }
    
    Biome getBiome();
    
    Set<String> getTags();
    
    default boolean isPlaceholder() {
        return false;
    }
    
    default boolean isSelf() {
        return false;
    }
    
    
}