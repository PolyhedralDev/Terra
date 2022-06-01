/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.config.pattern;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.feature.locator.patterns.MatchPattern;
import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
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
