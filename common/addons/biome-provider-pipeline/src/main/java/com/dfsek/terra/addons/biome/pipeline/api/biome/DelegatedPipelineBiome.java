package com.dfsek.terra.addons.biome.pipeline.api.biome;

import java.util.Set;

import com.dfsek.terra.api.world.biome.Biome;


public final class DelegatedPipelineBiome implements PipelineBiome {
    private final Biome biome;
    
    public DelegatedPipelineBiome(Biome biome) {
        this.biome = biome;
    }
    
    @Override
    public Biome getBiome() {
        return biome;
    }
    
    @Override
    public int hashCode() {
        return biome.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DelegatedPipelineBiome that)) return false;
        return that.biome.equals(this.biome);
    }
    
    @Override
    public Set<String> getTags() {
        return biome.getTags();
    }
    
    @Override
    public String getID() {
        return biome.getID();
    }
}
