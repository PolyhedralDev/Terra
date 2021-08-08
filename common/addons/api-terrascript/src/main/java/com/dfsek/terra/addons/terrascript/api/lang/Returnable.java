package com.dfsek.terra.addons.terrascript.api.lang;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.api.properties.Context;

import java.util.Map;

public interface Returnable<T> extends Item<T> {
    ReturnType returnType();

    enum ReturnType {
        NUMBER(true), STRING(true), BOOLEAN(false), VOID(false), OBJECT(false);

        private final boolean comparable;

        ReturnType(boolean comparable) {
            this.comparable = comparable;
        }

        public boolean isComparable() {
            return comparable;
        }
    }

    static <T> Returnable<T> constant(T value, ReturnType type, Position position) {
        return new Returnable<T>() {
            @Override
            public ReturnType returnType() {
                return type;
            }

            @Override
            public T apply(Context context, Map<String, Variable<?>> variableMap) {
                return value;
            }

            @Override
            public Position getPosition() {
                return position;
            }
        };
    }
}
