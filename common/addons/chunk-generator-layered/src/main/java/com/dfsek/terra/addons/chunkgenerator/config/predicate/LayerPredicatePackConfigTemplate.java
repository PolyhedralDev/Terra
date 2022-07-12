package com.dfsek.terra.addons.chunkgenerator.config.predicate;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.Map;

import com.dfsek.terra.addons.chunkgenerator.layer.predicate.LayerPredicate;
import com.dfsek.terra.api.config.meta.Meta;


public class LayerPredicatePackConfigTemplate implements ConfigTemplate {
    
    @Value("generation.predicates")
    private @Meta Map<String, LayerPredicate> predicates;
    
    public Map<String, LayerPredicate> getPredicates() {
        return predicates;
    }
    
}
