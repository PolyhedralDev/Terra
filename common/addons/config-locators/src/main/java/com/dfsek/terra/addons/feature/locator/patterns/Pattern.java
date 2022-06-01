/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator.patterns;

import com.dfsek.terra.api.world.chunk.generation.util.Column;


public interface Pattern {
    boolean matches(int y, Column<?> column);
    
    default Pattern and(Pattern that) {
        return (y, column) -> this.matches(y, column) && that.matches(y, column);
    }
    
    default Pattern or(Pattern that) {
        return (y, column) -> this.matches(y, column) || that.matches(y, column);
    }
    
    default Pattern xor(Pattern that) {
        return (y, column) -> this.matches(y, column) ^ that.matches(y, column);
    }
    
    default Pattern not() {
        return (y, column) -> !this.matches(y, column);
    }
}
