package com.dfsek.terra.addons.terrascript.v2;

import java.util.Optional;

public interface Type {
    
    static Type fromString(String lexeme) throws TypeException {
        return switch(lexeme) {
            case "num" -> NUMBER;
            case "int" -> INTEGER;
            case "str" -> STRING;
            case "bool" -> BOOLEAN;
            case "()" -> VOID;
            default -> throw new TypeException();
        };
    }
    
    java.lang.reflect.Type javaType();
    
    default boolean typeOf(Type type) {
        return this.equals(type);
    }
    
    static Optional<Type> from(Class<?> clazz) {
        return Optional.ofNullable(
                clazz == double.class ? NUMBER :
                clazz == String.class ? STRING :
                clazz == boolean.class ? BOOLEAN :
                clazz == void.class ? VOID :
                null);
    }
    
    Type NUMBER = new Type() {
        @Override
        public java.lang.reflect.Type javaType() {
            return double.class;
        }
        
        @Override
        public String toString() {
            return "num";
        }
    };
    
    Type INTEGER = new Type() {
        
        @Override
        public java.lang.reflect.Type javaType() {
            return int.class;
        }
        
        @Override
        public String toString() {
            return "int";
        }
    };
    
    Type STRING = new Type() {
        
        @Override
        public java.lang.reflect.Type javaType() {
            return String.class;
        }
        
        @Override
        public String toString() {
            return "str";
        }
    };
    
    Type BOOLEAN = new Type() {
        @Override
        public java.lang.reflect.Type javaType() {
            return boolean.class;
        }
        
        @Override
        public String toString() {
            return "bool";
        }
    };
    
    Type VOID = new Type() {
        @Override
        public java.lang.reflect.Type javaType() {
            return void.class;
        }
        
        @Override
        public String toString() {
            return "()";
        }
    };
    
    class TypeException extends Exception {
    }
}
