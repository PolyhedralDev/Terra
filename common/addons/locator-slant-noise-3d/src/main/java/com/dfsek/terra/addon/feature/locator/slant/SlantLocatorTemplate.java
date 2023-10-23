package com.dfsek.terra.addon.feature.locator.slant;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.structure.feature.Locator;

import java.util.function.DoublePredicate;


public class SlantLocatorTemplate implements ObjectTemplate<Locator> {
    
    @Value("condition")
    private DoublePredicate predicate;
    
    @Override
    public Locator get() {
        return new SlantLocator(predicate);
    }
}
