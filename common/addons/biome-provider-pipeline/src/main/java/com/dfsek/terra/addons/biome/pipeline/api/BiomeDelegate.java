package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.api.util.StringIdentifiable;
import com.dfsek.terra.api.world.biome.Biome;


public interface BiomeDelegate extends StringIdentifiable {
    Biome getBiome();
    
    static BiomeDelegate ephemeral(String id) {
        return new BiomeDelegate() {
            @Override
            public Biome getBiome() {
                throw new UnsupportedOperationException("Cannot get biome from ephemeral delegate");
            }
    
            @Override
            public String getID() {
                return id;
            }
        };
    }
}
