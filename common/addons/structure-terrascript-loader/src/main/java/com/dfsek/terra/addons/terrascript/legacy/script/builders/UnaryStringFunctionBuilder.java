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
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class UnaryStringFunctionBuilder implements FunctionBuilder<Function<Void>> {
    
    private final java.util.function.Consumer<String> function;
    
    public UnaryStringFunctionBuilder(java.util.function.Consumer<String> function) {
        this.function = function;
    }
    
    @Override
    public Function<Void> build(List<Expression<?>> argumentList, SourcePosition position) {
        return new Function<>() {
            @Override
            public Type returnType() {
                return Type.VOID;
            }
            
            @SuppressWarnings("unchecked")
            @Override
            public Void evaluate(ImplementationArguments implementationArguments, Scope scope) {
                function.accept(((Expression<String>) argumentList.get(0)).evaluate(implementationArguments, scope));
                return null;
            }
            
            @Override
            public SourcePosition getPosition() {
                return position;
            }
        };
    }
    
    @Override
    public int argNumber() {
        return 1;
    }
    
    @Override
    public Type getArgument(int position) {
        if(position == 0) return Type.STRING;
        return null;
    }
}
