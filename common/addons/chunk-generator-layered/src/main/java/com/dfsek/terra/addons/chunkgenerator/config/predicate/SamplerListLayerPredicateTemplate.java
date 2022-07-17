package com.dfsek.terra.addons.chunkgenerator.config.predicate;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.List;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.predicate.SamplerListLayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.layer.predicate.SamplerListLayerPredicate.CoordinateTest;
import com.dfsek.terra.addons.chunkgenerator.layer.predicate.SamplerLayerPredicate.Operator;
import com.dfsek.terra.addons.chunkgenerator.util.InstanceWrapper;
import com.dfsek.terra.api.config.meta.Meta;


public class SamplerListLayerPredicateTemplate implements ObjectTemplate<LayerPredicate> {
    
    @Value("sampler")
    private @Meta InstanceWrapper<LayerSampler> sampler;
    
    @Value("tests")
    private List<CoordinateTest> tests;
    
    @Value("default-threshold")
    @Default
    private double defaultThreshold = 0;
    
    @Value("default-operator")
    @Default
    private Operator defaultOperator = Operator.GreaterThan;
    
    @Override
    public LayerPredicate get() {
        return new SamplerListLayerPredicate(sampler.get(), defaultOperator, tests);
    }
}
