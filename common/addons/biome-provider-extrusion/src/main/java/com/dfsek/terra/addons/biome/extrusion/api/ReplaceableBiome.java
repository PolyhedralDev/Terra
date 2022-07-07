package com.dfsek.terra.addons.biome.extrusion.api;


import java.util.Optional;

import com.dfsek.terra.api.world.biome.Biome;


/**
 * Basically just a specialised implementation of {@link Optional} for biomes where a biome may be a "self" reference.
 */
public sealed interface ReplaceableBiome permits PresentBiome, SelfBiome {
    static ReplaceableBiome of(Biome biome) {
        return new PresentBiome(biome);
    }
    
    static ReplaceableBiome self() {
        return SelfBiome.INSTANCE;
    }
    
    Biome get(Biome existing);
    
    default Biome get() {
        if(isSelf()) {
            throw new IllegalStateException("Cannot get() self biome!");
        }
        return get(null);
    }
    
    boolean isSelf();
}
