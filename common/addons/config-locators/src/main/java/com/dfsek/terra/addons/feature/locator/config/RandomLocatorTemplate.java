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

import com.dfsek.terra.addons.feature.locator.locators.RandomLocator;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;


@SuppressWarnings("FieldMayBeFinal")
public class RandomLocatorTemplate implements ObjectTemplate<Locator> {
    @Value("height")
    private @Meta Range height;
    
    @Value("amount")
    private @Meta Range amount;
    
    @Value("salt")
    @Default
    private @Meta int salt = 0;
    
    @Override
    public Locator get() {
        return new RandomLocator(height, amount, salt);
    }
}
