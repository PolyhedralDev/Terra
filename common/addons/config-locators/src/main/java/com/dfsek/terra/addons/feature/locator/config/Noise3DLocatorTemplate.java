/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import com.dfsek.terra.addons.feature.locator.locators.Noise3DLocator;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Locator;


public class Noise3DLocatorTemplate implements ObjectTemplate<Locator> {
    @Value("sampler")
    private @Meta NoiseSampler sampler;
    
    @Override
    public Locator get() {
        return new Noise3DLocator(sampler);
    }
}
