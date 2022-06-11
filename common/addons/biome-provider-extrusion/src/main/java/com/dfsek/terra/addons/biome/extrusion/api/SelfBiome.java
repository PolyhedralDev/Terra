package com.dfsek.terra.addons.biome.extrusion.api;


import com.dfsek.terra.api.world.biome.Biome;

import java.util.Objects;


final class SelfBiome implements ReplaceableBiome {
    public static final SelfBiome INSTANCE = new SelfBiome();
    
    @Override
    public Biome get(Biome existing) {
        return Objects.requireNonNull(existing);
    }
    
    @Override
    public boolean isSelf() {
        return true;
    }
}
