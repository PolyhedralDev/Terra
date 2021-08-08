package com.dfsek.terra.addons.terrascript.api;

import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.api.properties.Context;

import java.util.Map;

public interface Function<T> extends Returnable<T> {
    Function<?> NULL = new Function<Object>() {
        @Override
        public ReturnType returnType() {
            return null;
        }

        @Override
        public Object apply(Context context, Map<String, Variable<?>> variableMap) {
            return null;
        }

        @Override
        public Position getPosition() {
            return null;
        }
    };
}
