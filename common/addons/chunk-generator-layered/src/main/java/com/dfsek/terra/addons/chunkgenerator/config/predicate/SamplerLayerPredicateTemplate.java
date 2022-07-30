package com.dfsek.terra.addons.chunkgenerator.config.predicate;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.predicate.SamplerLayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.math.BooleanOperator;
import com.dfsek.terra.addons.chunkgenerator.util.InstanceWrapper;
import com.dfsek.terra.api.config.meta.Meta;


public class SamplerLayerPredicateTemplate implements ObjectTemplate<LayerPredicate> {
    
    @Value("sampler")
    private @Meta InstanceWrapper<LayerSampler> sampler;
    
    @Value("threshold")
    @Default
    private double threshold = 0;
    
    @Value("operator")
    @Default
    private BooleanOperator operator = BooleanOperator.GreaterThan;
    
    @Override
    public LayerPredicate get() {
        return new SamplerLayerPredicate(sampler.get(), operator, threshold);
    }
}
