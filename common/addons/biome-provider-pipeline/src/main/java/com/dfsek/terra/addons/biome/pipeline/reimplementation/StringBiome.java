package com.dfsek.terra.addons.biome.pipeline.reimplementation;

import java.util.Optional;
import java.util.Set;

import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.PlatformBiome;

public record StringBiome(String name, String string) implements Biome {
    
    @Override
    public Context getContext() {
        return null;
    }
    
    @Override
    public String getID() {
        return string;
    }
    
    @Override
    public Optional<PlatformBiome> getPlatformBiome() {
        return Optional.empty();
    }
    
    @Override
    public void setPlatformBiome(PlatformBiome biome) {
    }
    
    @Override
    public int getColor() {
        return 0;
    }
    
    @Override
    public Set<String> getTags() {
        return Set.of(name);
    }
}