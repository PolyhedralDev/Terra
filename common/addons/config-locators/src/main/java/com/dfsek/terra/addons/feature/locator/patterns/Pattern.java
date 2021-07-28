package com.dfsek.terra.addons.feature.locator.patterns;

import com.dfsek.terra.api.world.Column;

public interface Pattern {
    boolean matches(int y, Column column);
}
