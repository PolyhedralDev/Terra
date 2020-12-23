package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.tokenizer.Position;

public interface Item<T> {
    T apply(Location location, Rotation rotation, int recursions);

    T apply(Location location, Chunk chunk, Rotation rotation, int recursions);

    Position getPosition();
}
