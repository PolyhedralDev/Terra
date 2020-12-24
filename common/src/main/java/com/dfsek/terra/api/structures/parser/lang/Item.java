package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

public interface Item<T> {
    T apply(Buffer buffer, Rotation rotation, int recursions);

    Position getPosition();
}
