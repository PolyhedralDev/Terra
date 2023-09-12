/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.constants;

import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public abstract class ConstantExpression<T> implements Expression<T> {
    private final T constant;
    private final SourcePosition position;
    
    public ConstantExpression(T constant, SourcePosition position) {
        this.constant = constant;
        this.position = position;
    }
    
    @Override
    public T evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return constant;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
    
    public T getConstant() {
        return constant;
    }
}