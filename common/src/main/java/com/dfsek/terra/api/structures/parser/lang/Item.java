package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.tokenizer.Position;

public interface Item<T> {
    T apply(Location location, Rotation rotation, int recursions);

    Position getPosition();
}
