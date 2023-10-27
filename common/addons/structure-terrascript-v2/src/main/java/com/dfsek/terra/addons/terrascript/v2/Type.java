package com.dfsek.terra.addons.terrascript.v2;

import com.google.common.collect.Streams;

import javax.annotation.Nullable;
import java.util.List;

import com.dfsek.terra.addons.terrascript.v2.codegen.CodegenType;
import com.dfsek.terra.addons.terrascript.v2.codegen.NativeFunction;
import com.dfsek.terra.api.util.generic.pair.Pair;


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
    
    CodegenType getCodegenType();
    
    class Function implements Type {
        
        private final Type returnType;
        private final List<Type> parameters;
        private final String id;
        
        public Function(Type returnType, List<Type> parameters, @Nullable String identifier, Environment declarationScope) {
            this.returnType = returnType;
            this.parameters = parameters;
            this.id = identifier == null ? "ANONYMOUS" : identifier + declarationScope.name;
        }
        
        public Type getReturnType() {
            return returnType;
        }
        
        public List<Type> getParameters() {
            return parameters;
        }
        
        public String getId() {
            return id;
        }
        
        @Override
        public boolean typeOf(Type type) {
            if(!(type instanceof Function function)) return false;
            return returnType.typeOf(function.returnType) && paramsAreSubtypes(parameters, function.parameters);
        }
        
        @Override
        public CodegenType getCodegenType() {
            return CodegenType.OBJECT;
        }
        
        private static boolean paramsAreSubtypes(List<Type> subtypes, List<Type> superTypes) {
            if(subtypes.size() != superTypes.size()) return false;
            return Streams.zip(subtypes.stream(), superTypes.stream(), Pair::of).allMatch(p -> p.getLeft().typeOf(p.getRight()));
        }
        
        @Override
        public java.lang.reflect.Type javaType() {
            return Function.class;
        }
        
        @Override
        public String toString() {
            return "(" + String.join(",", parameters.stream().map(Object::toString).toList()) + ") -> " + returnType;
        }
        
        public static class Native extends Function {
            private final NativeFunction nativeFunction;
            
            public Native(Type returnType, List<Type> parameters, @org.jetbrains.annotations.Nullable String identifier,
                          Environment declarationScope, NativeFunction nativeFunction) {
                super(returnType, parameters, identifier, declarationScope);
                this.nativeFunction = nativeFunction;
            }
            
            public NativeFunction getNativeFunction() {
                return nativeFunction;
            }
        }
    }
    
    Type NUMBER = new Type() {
        @Override
        public java.lang.reflect.Type javaType() {
            return double.class;
        }
        
        @Override
        public CodegenType getCodegenType() {
            return CodegenType.DOUBLE;
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
        public CodegenType getCodegenType() {
            return CodegenType.INTEGER;
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
        public CodegenType getCodegenType() {
            return CodegenType.STRING;
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
        public CodegenType getCodegenType() {
            return CodegenType.BOOLEAN;
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
        public CodegenType getCodegenType() {
            return CodegenType.VOID;
        }
        
        @Override
        public String toString() {
            return "()";
        }
    };
    
    class TypeException extends Exception {
    }
}
