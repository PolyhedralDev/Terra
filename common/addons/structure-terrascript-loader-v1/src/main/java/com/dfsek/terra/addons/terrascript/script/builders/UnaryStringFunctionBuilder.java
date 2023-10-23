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


public class UnaryStringFunctionBuilder implements FunctionBuilder<Function<Void>> {
    
    private final java.util.function.Consumer<String> function;
    
    public UnaryStringFunctionBuilder(java.util.function.Consumer<String> function) {
        this.function = function;
    }
    
    @Override
    public Function<Void> build(List<Returnable<?>> argumentList, Position position) {
        return new Function<>() {
            @Override
            public ReturnType returnType() {
                return ReturnType.VOID;
            }
            
            @SuppressWarnings("unchecked")
            @Override
            public Void apply(ImplementationArguments implementationArguments, Scope scope) {
                function.accept(((Returnable<String>) argumentList.get(0)).apply(implementationArguments, scope));
                return null;
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
        if(position == 0) return Returnable.ReturnType.STRING;
        return null;
    }
}
