package com.dfsek.terra.addons.terrascript.api;

import java.util.Map;

public interface Item<T> {
    T apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap);

    Position getPosition();
}
