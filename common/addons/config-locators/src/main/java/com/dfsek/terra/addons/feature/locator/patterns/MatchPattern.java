/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.patterns;

import java.util.function.Predicate;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.world.chunk.generation.util.Column;


public class MatchPattern implements Pattern {
    private final Range range;
    private final Predicate<BlockState> matches;
    
    public MatchPattern(Range range, Predicate<BlockState> matches) {
        this.range = range;
        this.matches = matches;
    }
    
    @Override
    public boolean matches(int y, Column<?> column) {
        for(int i : range) {
            if(!matches.test(column.getBlock(y + i))) return false;
        }
        return true;
    }
}
