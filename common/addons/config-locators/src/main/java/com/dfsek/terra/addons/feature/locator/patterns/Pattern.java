package com.dfsek.terra.addons.feature.locator.patterns;

import com.dfsek.terra.api.world.Column;


public interface Pattern {
    boolean matches(int y, Column column);
    
    default Pattern and(Pattern that) {
        return (y, column) -> this.matches(y, column) && that.matches(y, column);
    }
    
    default Pattern or(Pattern that) {
        return (y, column) -> this.matches(y, column) || that.matches(y, column);
    }
    
    default Pattern not() {
        return (y, column) -> !this.matches(y, column);
    }
}
