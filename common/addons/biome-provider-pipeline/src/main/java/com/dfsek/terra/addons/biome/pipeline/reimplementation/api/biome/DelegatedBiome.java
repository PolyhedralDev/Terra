package com.dfsek.terra.addons.biome.pipeline.reimplementation.api.biome;

import java.util.Set;

import com.dfsek.terra.api.world.biome.Biome;


public final class DelegatedBiome implements PipelineBiome {
    private final Biome biome;
    
    public DelegatedBiome(Biome biome) {
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
        if(!(obj instanceof DelegatedBiome that)) return false;
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
