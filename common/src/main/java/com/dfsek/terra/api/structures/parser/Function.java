package com.dfsek.terra.api.structures.parser;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;

public interface Function {
    void apply(Location location);

    void apply(Location location, Chunk chunk);

    String name();
}
