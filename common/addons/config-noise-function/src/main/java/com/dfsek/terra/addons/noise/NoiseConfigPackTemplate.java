package com.dfsek.terra.addons.noise;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;

import java.util.Map;

import com.dfsek.terra.addons.noise.config.DimensionApplicableNoiseSampler;
import com.dfsek.terra.api.config.meta.Meta;


public class NoiseConfigPackTemplate implements ConfigTemplate {
    @Value("noise")
    private @Meta Map<String, @Meta DimensionApplicableNoiseSampler> noiseBuilderMap;
    
    public Map<String, DimensionApplicableNoiseSampler> getNoiseBuilderMap() {
        return noiseBuilderMap;
    }
}
