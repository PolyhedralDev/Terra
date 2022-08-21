package com.dfsek.terra.addons.feature.distributor.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.feature.distributor.distributors.PaddedGridSamplerDistributor;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Distributor;


public class PaddedGridSamplerDistributorTemplate implements ObjectTemplate<Distributor> {
    @Value("width")
    private @Meta int width;
    
    @Value("padding")
    private @Meta int padding;
    
    @Value("sampler")
    private @Meta NoiseSampler noise;
    
    @Override
    public Distributor get() {
        return new PaddedGridSamplerDistributor(noise, width, padding);
    }
}
