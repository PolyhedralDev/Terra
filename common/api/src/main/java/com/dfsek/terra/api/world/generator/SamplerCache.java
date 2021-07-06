package com.dfsek.terra.api.world.generator;

public interface SamplerCache {
    Sampler get(int x, int z);

    Sampler getChunk(int cx, int cz);

    void clear();
}
