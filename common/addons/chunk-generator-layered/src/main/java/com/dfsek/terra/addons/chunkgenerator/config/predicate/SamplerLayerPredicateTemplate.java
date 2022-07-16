package com.dfsek.terra.addons.chunkgenerator.config.predicate;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.layer.predicate.SamplerLayerPredicate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


public class SamplerLayerPredicateTemplate implements ObjectTemplate<LayerPredicate> {
    
    @Value("sampler")
    private @Meta NoiseSampler sampler;
    
    @Override
    public LayerPredicate get() {
        return new SamplerLayerPredicate(sampler);
    }
}
