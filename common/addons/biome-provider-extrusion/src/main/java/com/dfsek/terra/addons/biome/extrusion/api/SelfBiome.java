package com.dfsek.terra.addons.biome.extrusion.api;


import java.util.Objects;

import com.dfsek.terra.api.world.biome.Biome;


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
