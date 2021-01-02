package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.structures.tokenizer.Position;

public interface Item<T> {
    T apply(ImplementationArguments implementationArguments);

    Position getPosition();
}
