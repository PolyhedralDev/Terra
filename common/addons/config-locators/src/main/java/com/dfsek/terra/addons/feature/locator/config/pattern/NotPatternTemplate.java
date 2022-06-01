/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.config.pattern;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.api.config.meta.Meta;


public class NotPatternTemplate implements ObjectTemplate<Pattern> {
    @Value("pattern")
    private @Meta Pattern pattern;
    
    @Override
    public Pattern get() {
        return pattern.not();
    }
}
