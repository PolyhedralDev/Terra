package com.dfsek.terra.addons.terrascript.api.lang;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.api.properties.Context;

import java.util.Map;

public interface Item<T> {
    T apply(Context context, Map<String, Variable<?>> variableMap);

    Position getPosition();
}
