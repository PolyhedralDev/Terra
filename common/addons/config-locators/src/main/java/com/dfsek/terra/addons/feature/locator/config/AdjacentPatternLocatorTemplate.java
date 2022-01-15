/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.feature.locator.locators.AdjacentPatternLocator;
import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;


@SuppressWarnings({ "FieldCanBeLocal", "FieldMayBeFinal" })
public class AdjacentPatternLocatorTemplate implements ObjectTemplate<Locator> {
    @Value("range")
    private @Meta Range range;
    
    @Value("pattern")
    private @Meta Pattern pattern;
    
    @Value("match-all")
    @Default
    private @Meta boolean matchAll = false;
    
    @Override
    public Locator get() {
        return new AdjacentPatternLocator(pattern, range, matchAll);
    }
}
