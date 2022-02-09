/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.config.pattern;

import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.exception.ValidationException;

import java.util.List;

import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.api.config.meta.Meta;


public class AndPatternTemplate implements ObjectTemplate<Pattern>, ValidatedConfigTemplate {
    @Value("patterns")
    private @Meta List<@Meta Pattern> patterns;
    
    @Override
    public Pattern get() {
        Pattern current = patterns.remove(0);
        while(!patterns.isEmpty()) {
            current = current.and(patterns.remove(0));
        }
        return current;
    }
    
    @Override
    public boolean validate() throws ValidationException {
        if(patterns.isEmpty()) throw new ValidationException("AND Pattern must specify at least 1 pattern.");
        return true;
    }
}
