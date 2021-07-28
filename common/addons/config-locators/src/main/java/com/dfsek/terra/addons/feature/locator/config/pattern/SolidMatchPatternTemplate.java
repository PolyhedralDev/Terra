package com.dfsek.terra.addons.feature.locator.config.pattern;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.addons.feature.locator.patterns.match.MatchPattern;
import com.dfsek.terra.api.util.Range;

public class SolidMatchPatternTemplate implements ObjectTemplate<Pattern> {
    @Value("offset")
    private Range offset;

    @Override
    public Pattern get() {
        return new MatchPattern(offset, blockState -> blockState.getBlockType().isSolid());
    }
}
