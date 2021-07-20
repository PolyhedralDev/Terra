package com.dfsek.terra.addons.feature.locator.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.locator.locators.SurfaceLocator;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;

public class SurfaceLocatorTemplate implements ObjectTemplate<Locator> {
    private final TerraPlugin main;

    @Value("range")
    private Range range;

    public SurfaceLocatorTemplate(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public Locator get() {
        return new SurfaceLocator(range, main);
    }
}
