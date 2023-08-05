/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class ZeroArgFunctionBuilder<T> implements FunctionBuilder<Function<T>> {
    private final java.util.function.Function<TerraImplementationArguments, T> function;
    private final Type type;
    
    public ZeroArgFunctionBuilder(java.util.function.Function<TerraImplementationArguments, T> function, Type type) {
        this.function = function;
        this.type = type;
    }
    
    @Override
    public Function<T> build(List<Expression<?>> argumentList, SourcePosition position) {
        return new Function<>() {
            @Override
            public Type returnType() {
                return type;
            }
            
            @Override
            public T evaluate(ImplementationArguments implementationArguments, Scope scope) {
                return function.apply((TerraImplementationArguments) implementationArguments);
            }
            
            @Override
            public SourcePosition getPosition() {
                return position;
            }
        };
    }
    
    @Override
    public int argNumber() {
        return 0;
    }
    
    @Override
    public Type getArgument(int position) {
        if(position == 0) return type;
        return null;
    }
}
