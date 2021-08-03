package com.dfsek.terra.addons.terrascript.api.lang;

import com.dfsek.terra.addons.terrascript.api.Position;

public interface Variable<T> {
    T getValue();

    void setValue(T value);

    Returnable.ReturnType getType();

    Position getPosition();
}
