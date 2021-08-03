package com.dfsek.terra.addons.terrascript.api;

public interface Variable<T> {
    T getValue();

    void setValue(T value);

    Returnable.ReturnType getType();

    Position getPosition();
}
