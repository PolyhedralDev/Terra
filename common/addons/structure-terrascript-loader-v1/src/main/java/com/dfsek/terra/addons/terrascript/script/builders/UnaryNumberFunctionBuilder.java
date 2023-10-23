/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class UnaryNumberFunctionBuilder implements FunctionBuilder<Function<Number>> {
    
    private final java.util.function.Function<Number, Number> function;
    
    public UnaryNumberFunctionBuilder(java.util.function.Function<Number, Number> function) {
        this.function = function;
    }
    
    @Override
    public Function<Number> build(List<Returnable<?>> argumentList, Position position) {
        return new Function<>() {
            @Override
            public ReturnType returnType() {
                return ReturnType.NUMBER;
            }
            
            @SuppressWarnings("unchecked")
            @Override
            public Number apply(ImplementationArguments implementationArguments, Scope scope) {
                return function.apply(((Returnable<Number>) argumentList.get(0)).apply(implementationArguments, scope));
            }
            
            @Override
            public Position getPosition() {
                return position;
            }
        };
    }
    
    @Override
    public int argNumber() {
        return 1;
    }
    
    @Override
    public Returnable.ReturnType getArgument(int position) {
        if(position == 0) return Returnable.ReturnType.NUMBER;
        return null;
    }
}
