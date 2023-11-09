/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.config.pattern;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.feature.locator.patterns.MatchPattern;
import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.Range;


public class SingleBlockMatchPatternTemplate implements ObjectTemplate<Pattern> {
    @Value("block")
    private @Meta BlockState block;

    @Value("offset")
    private @Meta Range offset;


    @Override
    public Pattern get() {
        return new MatchPattern(offset, block::matches);
    }
}
