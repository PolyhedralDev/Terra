package com.dfsek.terra.addons.chunkgenerator.config.pack;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.LinkedHashMap;
import java.util.Map;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.api.config.meta.Meta;


public class LayerPredicatePackConfigTemplate implements ConfigTemplate {
    
    @Value("generation.predicates")
    @Default
    private @Meta Map<String, LayerPredicate> predicates = new LinkedHashMap<>();
    
    public Map<String, LayerPredicate> getPredicates() {
        return predicates;
    }
    
}
