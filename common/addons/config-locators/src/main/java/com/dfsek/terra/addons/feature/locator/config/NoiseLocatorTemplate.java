package com.dfsek.terra.addons.feature.locator.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.List;

import com.dfsek.terra.addons.feature.locator.locators.NoiseLocator;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.feature.Locator;


public class NoiseLocatorTemplate implements ObjectTemplate<Locator> {
    @Value("samplers")
    private @Meta List<@Meta NoiseSampler> samplers;
    
    @Override
    public Locator get() {
        return new NoiseLocator(samplers);
    }
}
