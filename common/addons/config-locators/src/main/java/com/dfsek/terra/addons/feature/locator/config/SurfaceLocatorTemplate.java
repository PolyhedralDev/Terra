package com.dfsek.terra.addons.feature.locator.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import com.dfsek.terra.addons.feature.locator.locators.SurfaceLocator;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;


public class SurfaceLocatorTemplate implements ObjectTemplate<Locator> {
    private final Platform platform;
    
    @Value("range")
    private @Meta Range range;
    
    public SurfaceLocatorTemplate(Platform platform) {
        this.platform = platform;
    }
    
    @Override
    public Locator get() {
        return new SurfaceLocator(range, platform);
    }
}
