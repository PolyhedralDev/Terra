package com.dfsek.terra.addons.feature.locator.config.pattern;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.addons.feature.locator.patterns.MatchPattern;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.MaterialSet;

public class BlockSetMatchPatternTemplate implements ObjectTemplate<Pattern> {
    @Value("blocks")
    private @Meta MaterialSet blocks;

    @Value("offset")
    private @Meta Range offset;

    @Override
    public Pattern get() {
        return new MatchPattern(offset, blockState -> blocks.contains(blockState.getBlockType()));
    }
}
