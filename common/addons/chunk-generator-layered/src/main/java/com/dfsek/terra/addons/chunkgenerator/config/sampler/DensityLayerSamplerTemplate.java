package com.dfsek.terra.addons.chunkgenerator.config.sampler;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.sampler.DensityLayerSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.seismic.type.sampler.Sampler;


public class DensityLayerSamplerTemplate extends LayerSamplerTemplate {
    
    @Value("sampler")
    private @Meta Sampler sampler;
    
    @Override
    public LayerSampler get() {
        return new DensityLayerSampler(sampler, blend);
    }
}
