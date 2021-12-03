package com.dfsek.terra.addons.chunkgenerator.config;

import com.dfsek.tectonic.config.ConfigTemplate;


public class NoiseChunkGeneratorPackConfig implements ConfigTemplate {
    private final int elevationBlend;
    
    public NoiseChunkGeneratorPackConfig(int elevationBlend) {
        this.elevationBlend = elevationBlend;
    }
}
