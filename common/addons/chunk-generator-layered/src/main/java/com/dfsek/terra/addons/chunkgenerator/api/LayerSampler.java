package com.dfsek.terra.addons.chunkgenerator.api;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.world.biome.Biome;


public interface LayerSampler {
    double sample(long seed, Biome biome, int x, int y, int z);
    
    NoiseSampler getSampler();
}
