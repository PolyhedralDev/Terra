package com.dfsek.terra.addons.biome.pipeline.api.delegate;

import com.dfsek.terra.api.world.biome.Biome;

import java.util.Collections;
import java.util.Set;


final class EphemeralBiomeDelegate implements BiomeDelegate {
    private final Set<String> tags;
    private final String id;
    
    public EphemeralBiomeDelegate(String id) {
        this.id = id;
        tags = Collections.singleton(id);
    }
    
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
}
