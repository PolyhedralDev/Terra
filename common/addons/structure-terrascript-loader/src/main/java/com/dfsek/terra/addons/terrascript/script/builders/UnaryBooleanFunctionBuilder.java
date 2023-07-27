/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;
import java.util.function.BiConsumer;

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;


public class UnaryBooleanFunctionBuilder implements FunctionBuilder<Function<Void>> {
    
    private final BiConsumer<Boolean, TerraImplementationArguments> function;
    
    public UnaryBooleanFunctionBuilder(BiConsumer<Boolean, TerraImplementationArguments> function) {
        this.function = function;
    }
    
    @Override
    public Function<Void> build(List<Expression<?>> argumentList, SourcePosition position) {
        return new Function<>() {
            @Override
            public ReturnType returnType() {
                return ReturnType.VOID;
            }
            
            @SuppressWarnings("unchecked")
            @Override
            public Void evaluate(ImplementationArguments implementationArguments, Scope scope) {
                function.accept(((Expression<Boolean>) argumentList.get(0)).evaluate(implementationArguments, scope),
                                (TerraImplementationArguments) implementationArguments);
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
    public Expression.ReturnType getArgument(int position) {
        if(position == 0) return Expression.ReturnType.BOOLEAN;
        return null;
    }
}
