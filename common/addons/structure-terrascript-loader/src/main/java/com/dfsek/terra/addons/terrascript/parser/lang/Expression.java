/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang;

import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public interface Expression<T> {
    ReturnType returnType();
    
    T evaluate(ImplementationArguments implementationArguments, Scope scope);
    
    default double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        throw new UnsupportedOperationException("Cannot apply " + this + " as double");
    }
    
    default boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        throw new UnsupportedOperationException("Cannot apply " + this + " as double");
    }
    
    SourcePosition getPosition();
    
    Expression<Void> NOOP = new Expression<>() {
        @Override
        public ReturnType returnType() {
            return ReturnType.VOID;
        }
        
        @Override
        public Void evaluate(ImplementationArguments implementationArguments, Scope scope) {
            return null;
        }
        
        @Override
        public SourcePosition getPosition() {
            return null;
        }
    };
    
    enum ReturnType {
        NUMBER(true),
        STRING(true),
        BOOLEAN(false),
        VOID(false),
        OBJECT(false);
        
        private final boolean comparable;
        
        ReturnType(boolean comparable) {
            this.comparable = comparable;
        }
        
        public boolean isComparable() {
            return comparable;
        }
    }
}
