package com.dfsek.terra.addons.chunkgenerator.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import com.dfsek.terra.api.config.meta.Meta;


public class NoiseChunkGeneratorPackConfigTemplate implements ConfigTemplate {
    @Value("blend.terrain.elevation")
    @Default
    private @Meta int elevationBlend = 4;
    
    public int getElevationBlend() {
        return elevationBlend;
    }
}
