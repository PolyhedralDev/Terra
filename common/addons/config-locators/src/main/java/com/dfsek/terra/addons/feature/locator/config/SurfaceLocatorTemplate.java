package com.dfsek.terra.addons.feature.locator.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.locator.locators.SurfaceLocator;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.seeded.SeededBuilder;

public class SurfaceLocatorTemplate implements ObjectTemplate<SeededBuilder<Locator>> {
    private final TerraPlugin main;

    @Value("range")
    private Range range;

    public SurfaceLocatorTemplate(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public SeededBuilder<Locator> get() {
        return seed -> new SurfaceLocator(range, main);
    }
}
