package com.dfsek.terra.addons.structure.structures.parser.lang.variables;

import com.dfsek.terra.addons.structure.structures.parser.lang.Returnable;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

public interface Variable<T> {
    T getValue();

    void setValue(T value);

    Returnable.ReturnType getType();

    Position getPosition();
}
