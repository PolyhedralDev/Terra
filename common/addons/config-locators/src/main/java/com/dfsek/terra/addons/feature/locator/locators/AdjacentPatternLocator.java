/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.locators;

import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.api.structure.feature.BinaryColumn;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.world.chunk.generation.util.Column;


public class AdjacentPatternLocator implements Locator {
    private final Pattern pattern;
    private final Range search;
    private final boolean matchAll;
    
    public AdjacentPatternLocator(Pattern pattern, Range search, boolean matchAll) {
        this.pattern = pattern;
        this.search = search;
        this.matchAll = matchAll;
    }
    
    @Override
    public BinaryColumn getSuitableCoordinates(Column<?> column) {
        return new BinaryColumn(search, y -> isValid(y, column));
    }
    
    private boolean isValid(int y, Column<?> column) {
        if(matchAll) {
            return pattern.matches(y, column.adjacent(0, -1)) &&
                   pattern.matches(y, column.adjacent(0, 1)) &&
                   pattern.matches(y, column.adjacent(-1, 0)) &&
                   pattern.matches(y, column.adjacent(1, 0));
        } else {
            return pattern.matches(y, column.adjacent(0, -1)) ||
                   pattern.matches(y, column.adjacent(0, 1)) ||
                   pattern.matches(y, column.adjacent(-1, 0)) ||
                   pattern.matches(y, column.adjacent(1, 0));
        }
    }
}
