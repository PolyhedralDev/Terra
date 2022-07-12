package com.dfsek.terra.addons.chunkgenerator.config.resolve;

import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.exception.ValidationException;

import java.util.Map;

import com.dfsek.terra.addons.chunkgenerator.layer.predicate.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.layer.resolve.LayerResolver;
import com.dfsek.terra.addons.chunkgenerator.layer.resolve.PredicateLayerResolver;
import com.dfsek.terra.api.config.meta.Meta;


public class PredicateLayerResolverTemplate implements ObjectTemplate<LayerResolver>, ValidatedConfigTemplate {
    
    private final Map<String, LayerPredicate> predicates;
    
    public PredicateLayerResolverTemplate(Map<String, LayerPredicate> predicates) {
        this.predicates = predicates;
    }
    
    @Value("if")
    private @Meta String predicate;
    
    @Value("then")
    private @Meta LayerResolver trueResolver;
    
    @Value("else")
    private @Meta LayerResolver falseResolver;
    
    
    @Override
    public PredicateLayerResolver get() {
        return new PredicateLayerResolver(predicates.get(predicate), trueResolver, falseResolver);
    }
    
    @Override
    public boolean validate() throws ValidationException {
        if (!predicates.containsKey(predicate)) throw new ValidationException("The predicate " + predicate + " does not exist!");
        return true;
    }
}
