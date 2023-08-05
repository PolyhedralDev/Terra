/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.constants;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class NumericConstant extends ConstantExpression<Number> {
    private final double constant;
    
    public NumericConstant(Number constant, SourcePosition position) {
        super(constant, position);
        this.constant = constant.doubleValue();
    }
    
    @Override
    public double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        return constant;
    }
    
    @Override
    public Type returnType() {
        return Type.NUMBER;
    }
}
