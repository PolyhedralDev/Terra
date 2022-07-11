package com.dfsek.terra.addons.chunkgenerator.config.predicate;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.layer.predicate.BelowLayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.layer.predicate.LayerPredicate;
import com.dfsek.terra.api.config.meta.Meta;


public class BelowLayerPredicateTemplate implements ObjectTemplate<LayerPredicate> {
    
    @Value("y")
    private @Meta int y;
    
    @Override
    public LayerPredicate get() {
        return new BelowLayerPredicate(y);
    }
}
