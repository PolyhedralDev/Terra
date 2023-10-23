/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.constants;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public abstract class ConstantExpression<T> implements Returnable<T> {
    private final T constant;
    private final Position position;
    
    public ConstantExpression(T constant, Position position) {
        this.constant = constant;
        this.position = position;
    }
    
    @Override
    public T apply(ImplementationArguments implementationArguments, Scope scope) {
        return constant;
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
    
    public T getConstant() {
        return constant;
    }
}
