package com.dfsek.terra.addons.chunkgenerator.config.predicate;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.math.RelationalOperator;
import com.dfsek.terra.addons.chunkgenerator.layer.predicate.SamplerListLayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.util.InstanceWrapper;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.api.config.meta.Meta;


public class SamplerListLayerPredicateTemplate implements ObjectTemplate<LayerPredicate> {
    
    @Value("sampler")
    private @Meta InstanceWrapper<LayerSampler> sampler;
    
    @Value("point-set")
    private PointSet points;
    
    @Value("threshold")
    @Default
    private double defaultThreshold = 0;
    
    @Value("operator")
    @Default
    private RelationalOperator defaultOperator = RelationalOperator.GreaterThan;
    
    @Override
    public LayerPredicate get() {
        return new SamplerListLayerPredicate(sampler.get(), defaultThreshold, defaultOperator, points);
    }
}
