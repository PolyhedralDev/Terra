package com.dfsek.terra.addons.terrascript;

import java.util.Optional;


// TODO - Make not enum
// TODO - Add integer type
public enum Type {
    NUMBER,
    STRING,
    BOOLEAN,
    VOID;
    
    public java.lang.reflect.Type javaType() {
        return switch(this) {
            case NUMBER -> double.class;
            case STRING -> String.class;
            case BOOLEAN -> boolean.class;
            case VOID -> void.class;
        };
    }
    
    public static Optional<Type> from(Class<?> clazz) {
        return Optional.ofNullable(
                clazz == double.class ? NUMBER :
                clazz == String.class ? STRING :
                clazz == boolean.class ? BOOLEAN :
                clazz == void.class ? VOID :
                null);
    }
}
