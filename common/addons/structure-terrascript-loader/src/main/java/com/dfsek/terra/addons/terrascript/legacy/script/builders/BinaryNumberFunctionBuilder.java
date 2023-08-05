/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.script.builders;

import java.util.List;
import java.util.function.BiFunction;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class BinaryNumberFunctionBuilder implements FunctionBuilder<Function<Number>> {
    
    private final BiFunction<Number, Number, Number> function;
    
    public BinaryNumberFunctionBuilder(BiFunction<Number, Number, Number> function) {
        this.function = function;
    }
    
    @Override
    public Function<Number> build(List<Expression<?>> argumentList, SourcePosition position) {
        return new Function<>() {
            @Override
            public Type returnType() {
                return Type.NUMBER;
            }
            
            @SuppressWarnings("unchecked")
            @Override
            public Number evaluate(ImplementationArguments implementationArguments, Scope scope) {
                return function.apply(((Expression<Number>) argumentList.get(0)).evaluate(implementationArguments, scope),
                                      ((Expression<Number>) argumentList.get(1)).evaluate(implementationArguments, scope));
            }
            
            @Override
            public SourcePosition getPosition() {
                return position;
            }
        };
    }
    
    @Override
    public int argNumber() {
        return 2;
    }
    
    @Override
    public Type getArgument(int position) {
        if(position == 0 || position == 1) return Type.NUMBER;
        return null;
    }
}
