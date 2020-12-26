package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Random;

public interface Item<T> {
    T apply(Buffer buffer, Rotation rotation, Random random, int recursions);

    Position getPosition();
}
