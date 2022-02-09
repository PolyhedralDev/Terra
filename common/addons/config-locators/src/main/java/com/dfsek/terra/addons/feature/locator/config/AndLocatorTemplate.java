/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.config;

import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.exception.ValidationException;

import java.util.List;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Locator;


public class AndLocatorTemplate implements ObjectTemplate<Locator>, ValidatedConfigTemplate {
    @Value("locators")
    private @Meta List<@Meta Locator> locators;
    
    @Override
    public Locator get() {
        Locator current = locators.remove(0);
        while(!locators.isEmpty()) {
            current = current.and(locators.remove(0));
        }
        return current;
    }
    
    @Override
    public boolean validate() throws ValidationException {
        if(locators.isEmpty()) throw new ValidationException("AND Pattern must specify at least 1 pattern.");
        return true;
    }
}
