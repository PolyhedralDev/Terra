/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.script.functions;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.legacy.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class RandomFunction implements Function<Integer> {
    private final Expression<Number> numberReturnable;
    private final SourcePosition position;
    
    public RandomFunction(Expression<Number> numberReturnable, SourcePosition position) {
        this.numberReturnable = numberReturnable;
        this.position = position;
    }
    
    
    @Override
    public Type returnType() {
        return Type.NUMBER;
    }
    
    @Override
    public Integer evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return ((TerraImplementationArguments) implementationArguments).getRandom().nextInt(
                numberReturnable.evaluate(implementationArguments, scope).intValue());
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}