package com.dfsek.terra.addons.chunkgenerator.config;

import com.dfsek.tectonic.config.ConfigTemplate;


public class WorldSamplerContext implements ConfigTemplate {
    private final int elevationBlend;
    
    public WorldSamplerContext(int elevationBlend) {
        this.elevationBlend = elevationBlend;
    }
}
