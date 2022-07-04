/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.patterns;

import net.jafama.FastMath;

import java.util.function.Predicate;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.world.WritableWorld;
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
        int min = FastMath.max(column.getMinY(), range.getMin() + y);
        int max = FastMath.min(column.getMaxY(), range.getMax() + y);
        if(max <= min) return false;
        for(int i = min; i < max; i++) {
            if(!matches.test(column.getBlock(i))) return false;
        }
        return true;
    }
    
    @Override
    public boolean matches(WritableWorld world, int x, int y, int z) {
        int min = FastMath.max(world.getMinHeight(), range.getMin() + y);
        int max = FastMath.min(world.getMaxHeight(), range.getMax() + y);
        if(max <= min) return false;
        for(int i = min; i < max; i++) {
            if(!matches.test(world.getBlockState(x, i, z))) return false;
        }
        return true;
    }
}
