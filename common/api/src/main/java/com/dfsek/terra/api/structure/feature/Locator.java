package com.dfsek.terra.api.structure.feature;

import com.dfsek.terra.api.world.Column;

public interface Locator {
    BinaryColumn getSuitableCoordinates(Column column);
}
