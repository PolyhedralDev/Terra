package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.structure.Rotation;

public interface Item<T> {
    T apply(Location location, Rotation rotation);

    T apply(Location location, Chunk chunk, Rotation rotation);

    Position getPosition();
}
