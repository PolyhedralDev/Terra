package com.dfsek.terra.addons.chunkgenerator.config.predicate;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.layer.predicate.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.layer.predicate.RangeLayerPredicate;
import com.dfsek.terra.api.util.Range;


public class RangeLayerPredicateTemplate implements ObjectTemplate<LayerPredicate> {
    
    @Value("range")
    private Range range;
    
    @Override
    public LayerPredicate get() {
        return new RangeLayerPredicate(range);
    }
}
