package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;

public interface Item<T> {
    T apply(Location location);

    T apply(Location location, Chunk chunk);
}
