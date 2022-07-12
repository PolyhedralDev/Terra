package com.dfsek.terra.addons.chunkgenerator.config.resolve;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.chunkgenerator.layer.resolve.LayerResolver;
import com.dfsek.terra.api.config.meta.Meta;


public class LayerResolverPackConfigTemplate implements ConfigTemplate {
    
    @Value("generation.resolver")
    private @Meta LayerResolver resolver;
    
    public LayerResolver getResolver() {
        return resolver;
    }
}
