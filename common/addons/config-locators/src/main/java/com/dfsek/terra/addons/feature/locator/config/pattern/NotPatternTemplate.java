package com.dfsek.terra.addons.feature.locator.config.pattern;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.locator.patterns.Pattern;

public class NotPatternTemplate implements ObjectTemplate<Pattern> {
    @Value("pattern")
    private Pattern pattern;

    @Override
    public Pattern get() {
        return pattern.not();
    }
}
