package com.dfsek.terra.addons.terrascript.api;

import java.util.Map;

public interface Function<T> extends Returnable<T> {
    Function<?> NULL = new Function<Object>() {
        @Override
        public ReturnType returnType() {
            return null;
        }

        @Override
        public Object apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
            return null;
        }

        @Override
        public Position getPosition() {
            return null;
        }
    };
}
