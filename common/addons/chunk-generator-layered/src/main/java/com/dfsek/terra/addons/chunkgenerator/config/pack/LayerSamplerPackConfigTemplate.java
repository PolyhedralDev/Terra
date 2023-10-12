package com.dfsek.terra.addons.chunkgenerator.config.pack;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.Map;

import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.api.config.meta.Meta;


public class LayerSamplerPackConfigTemplate implements ConfigTemplate {
    
    @Value("generation.samplers")
    private @Meta Map<String, LayerSampler> samplers;
    
    public Map<String, LayerSampler> getSamplers() {
        return samplers;
    }
    
}
