package com.dfsek.terra.addons.chunkgenerator.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;


public class NoiseChunkGeneratorPackConfigTemplate implements ConfigTemplate {
    @Value("blend.terrain.elevation")
    @Default
    private @Meta int elevationBlend = 4;
    
    @Value("carving.resolution.horizontal")
    @Default
    private @Meta int horizontalRes = 4;
    
    @Value("carving.resolution.vertical")
    @Default
    private @Meta int verticalRes = 2;
    
    public int getElevationBlend() {
        return elevationBlend;
    }
    
    public int getHorizontalRes() {
        return horizontalRes;
    }
    
    public int getVerticalRes() {
        return verticalRes;
    }
}
