package com.dfsek.terra.addons.chunkgenerator.config.sampler;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.BiomeDefinedLayerSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.seismic.type.sampler.Sampler;


public class BiomeDefinedLayerSamplerTemplate extends LayerSamplerTemplate {
    
    @Value("default")
    @Default
    private @Meta Sampler defaultSampler = null;
    
    @Override
    public LayerSampler get() {
        return new BiomeDefinedLayerSampler(defaultSampler, blend);
    }
}
