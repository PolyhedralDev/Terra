package com.dfsek.terra.addons.biome.pipeline.api.delegate;

import java.util.Collections;
import java.util.Set;

import com.dfsek.terra.api.world.biome.Biome;


final class SelfDelegate implements BiomeDelegate {
    public static final SelfDelegate INSTANCE = new SelfDelegate();
    
    private SelfDelegate() {

    }
    
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
}
