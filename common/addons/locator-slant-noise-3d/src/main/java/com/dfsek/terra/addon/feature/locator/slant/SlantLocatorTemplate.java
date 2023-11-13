package com.dfsek.terra.addon.feature.locator.slant;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.function.DoublePredicate;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Locator;


public class SlantLocatorTemplate implements ObjectTemplate<Locator> {
    
    @Value("condition")
    private @Meta DoublePredicate predicate;
    
    @Override
    public Locator get() {
        return new SlantLocator(predicate);
    }
}
