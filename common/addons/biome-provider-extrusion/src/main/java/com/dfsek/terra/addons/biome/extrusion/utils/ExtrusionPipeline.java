package com.dfsek.terra.addons.biome.extrusion.utils;

import com.dfsek.terra.api.world.biome.Biome;


public interface ExtrusionPipeline {
    Biome extrude(Biome original, int x, int y, int z, long seed);
}