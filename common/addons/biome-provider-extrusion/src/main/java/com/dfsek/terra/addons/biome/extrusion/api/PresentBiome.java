package com.dfsek.terra.addons.biome.extrusion.api;


import com.dfsek.terra.api.world.biome.Biome;


final class PresentBiome implements ReplaceableBiome {
    private final Biome biome;
    
    PresentBiome(Biome biome) {
        this.biome = biome;
    }
    
    @Override
    public Biome get(Biome existing) {
        return biome;
    }
    
    @Override
    public boolean isSelf() {
        return false;
    }
}
