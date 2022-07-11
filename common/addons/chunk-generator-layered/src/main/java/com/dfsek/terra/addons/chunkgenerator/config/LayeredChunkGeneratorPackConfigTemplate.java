package com.dfsek.terra.addons.chunkgenerator.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.chunkgenerator.layer.palette.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.layer.resolve.LayerResolver;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;

import java.util.Map;


public class LayeredChunkGeneratorPackConfigTemplate implements ConfigTemplate {
    @Value("generation.sampler")
    private @Meta NoiseSampler sampler;
    
    @Value("generation.resolver")
    private @Meta LayerResolver resolver;
    
    @Value("generation.palettes")
    private @Meta Map<String, LayerPalette> palettes;
    
    public Map<String, LayerPalette> getPalettes() {
        return palettes;
    }
    
    public NoiseSampler getSampler() {
        return sampler;
    }
    
    public LayerResolver getResolver() {
        return resolver;
    }
}
