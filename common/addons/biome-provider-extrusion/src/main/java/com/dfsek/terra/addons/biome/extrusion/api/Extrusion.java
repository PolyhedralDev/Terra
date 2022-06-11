package com.dfsek.terra.addons.biome.extrusion.api;

import com.dfsek.terra.api.world.biome.Biome;

import java.util.Collection;


public interface Extrusion {
    Biome extrude(Biome original, int x, int y, int z, long seed);
    
    Collection<Biome> getBiomes();
}
