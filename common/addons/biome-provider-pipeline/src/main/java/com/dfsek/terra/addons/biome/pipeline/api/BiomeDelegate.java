package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.api.util.StringIdentifiable;
import com.dfsek.terra.api.world.biome.Biome;

import java.util.Collections;
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
        return new BiomeDelegate() {
            private final Set<String> tags = Collections.singleton(id);
            @Override
            public Biome getBiome() {
                throw new UnsupportedOperationException("Cannot get biome from ephemeral delegate");
            }
    
            @Override
            public Set<String> getTags() {
                return tags;
            }
    
            @Override
            public String getID() {
                return id;
            }
    
            @Override
            public boolean isEphemeral() {
                return true;
            }
        };
    }
    
    static BiomeDelegate from(Biome biome) {
        return new BiomeDelegate() {
            @Override
            public Biome getBiome() {
                return biome;
            }
    
            @Override
            public Set<String> getTags() {
                return biome.getTags();
            }
    
            @Override
            public String getID() {
                return biome.getID();
            }
        };
    }
    
    static BiomeDelegate self() {
        return new BiomeDelegate() {
            @Override
            public Biome getBiome() {
                throw new UnsupportedOperationException("Cannot get biome from self delegate");
            }
    
            @Override
            public boolean isSelf() {
                return true;
            }
    
            @Override
            public boolean isEphemeral() {
                return true;
            }
    
            @Override
            public Set<String> getTags() {
                return Collections.emptySet();
            }
    
            @Override
            public String getID() {
                return "SELF";
            }
        };
    }
}
