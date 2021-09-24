package com.dfsek.terra.api.world.generator;

public interface SamplerCache {
    Sampler get(int x, int z);
    
    void clear();
    
    Sampler getChunk(int cx, int cz);
}
