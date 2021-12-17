/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.List;

import com.dfsek.terra.addons.feature.locator.locators.SamplerLocator;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Locator;


public class SamplerLocatorTemplate implements ObjectTemplate<Locator> {
    @Value("samplers")
    private @Meta List<@Meta NoiseSampler> samplers;
    
    @Override
    public Locator get() {
        return new SamplerLocator(samplers);
    }
}
