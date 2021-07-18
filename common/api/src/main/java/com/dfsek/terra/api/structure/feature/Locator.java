package com.dfsek.terra.api.structure.feature;

import com.dfsek.terra.api.world.Column;

import java.util.List;

public interface Locator {
    List<Integer> getSuitableCoordinates(Column column);
}
