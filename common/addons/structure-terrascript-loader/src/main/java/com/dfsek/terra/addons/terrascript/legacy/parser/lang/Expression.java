/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public interface Expression<T> {
    Expression<Void> NOOP = new Expression<>() {
        @Override
        public Type returnType() {
            return Type.VOID;
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
    
    Type returnType();
    
    T evaluate(ImplementationArguments implementationArguments, Scope scope);
    
    default double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        throw new UnsupportedOperationException("Cannot apply " + this + " as double");
    }
    
    default boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        throw new UnsupportedOperationException("Cannot apply " + this + " as double");
    }
    
    SourcePosition getPosition();
    
    
}
