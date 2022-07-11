package com.dfsek.terra.addons.chunkgenerator.config.resolve;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.layer.predicate.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.layer.resolve.LayerResolver;
import com.dfsek.terra.addons.chunkgenerator.layer.resolve.PredicateLayerResolver;
import com.dfsek.terra.api.config.meta.Meta;


public class PredicateLayerResolverTemplate implements ObjectTemplate<LayerResolver> {
    
    @Value("if")
    private @Meta LayerPredicate predicate;
    
    @Value("then")
    private @Meta LayerResolver trueResolver;
    
    @Value("else")
    private @Meta LayerResolver falseResolver;
    
    
    @Override
    public PredicateLayerResolver get() {
        return new PredicateLayerResolver(predicate, trueResolver, falseResolver);
    }
}
