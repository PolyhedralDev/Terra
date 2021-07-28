package com.dfsek.terra.addons.feature.locator.config.pattern;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.api.config.meta.Meta;

public class NotPatternTemplate implements ObjectTemplate<Pattern> {
    @Value("pattern")
    private @Meta Pattern pattern;

    @Override
    public Pattern get() {
        return pattern.not();
    }
}
