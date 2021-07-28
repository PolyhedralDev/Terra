package com.dfsek.terra.addons.feature.locator.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.locator.locators.PatternLocator;
import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;

public class PatternLocatorTemplate implements ObjectTemplate<Locator> {
    @Value("range")
    private @Meta Range range;

    @Value("pattern")
    private @Meta Pattern pattern;

    @Override
    public Locator get() {
        return new PatternLocator(pattern, range);
    }
}
