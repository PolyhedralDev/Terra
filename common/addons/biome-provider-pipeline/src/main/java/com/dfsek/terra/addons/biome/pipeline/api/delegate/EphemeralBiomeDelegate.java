package com.dfsek.terra.addons.biome.pipeline.api.delegate;

import java.util.HashSet;
import java.util.Set;

import com.dfsek.terra.api.world.biome.Biome;


final class EphemeralBiomeDelegate implements BiomeDelegate {
    private final Set<String> tags;
    private final String id;
    
    public EphemeralBiomeDelegate(String id) {
        this.id = id;
        tags = new HashSet<>();
        tags.add(id);
        tags.add("ALL");
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
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof EphemeralBiomeDelegate that)) return false;
        
        return this.id.equals(that.id);
    }
}
