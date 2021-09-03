package com.dfsek.terra.addons.noise.paralithic.noise;

import com.dfsek.paralithic.functions.dynamic.Context;


public class SeedContext implements Context {
    private final long seed;
    
    public SeedContext(long seed) {
        this.seed = seed;
    }
    
    public long getSeed() {
        return seed;
    }
}
