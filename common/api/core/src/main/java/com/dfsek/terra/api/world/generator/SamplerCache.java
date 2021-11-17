package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.util.math.Sampler;


public interface SamplerCache {
    Sampler get(int x, int z);
    
    void clear();
    
    Sampler getChunk(int cx, int cz);
}
