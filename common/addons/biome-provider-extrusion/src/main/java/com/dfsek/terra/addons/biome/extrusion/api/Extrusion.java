package com.dfsek.terra.addons.biome.extrusion.api;

import java.util.Collection;

import com.dfsek.terra.api.world.biome.Biome;


public interface Extrusion {
    Biome extrude(Biome original, int x, int y, int z, long seed);
    
    Collection<Biome> getBiomes();
}
