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
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class ZeroArgFunctionBuilder<T> implements FunctionBuilder<Function<T>> {
    private final java.util.function.Function<TerraImplementationArguments, T> function;
    private final Returnable.ReturnType type;
    
    public ZeroArgFunctionBuilder(java.util.function.Function<TerraImplementationArguments, T> function, Returnable.ReturnType type) {
        this.function = function;
        this.type = type;
    }
    
    @Override
    public Function<T> build(List<Returnable<?>> argumentList, Position position) {
        return new Function<>() {
            @Override
            public ReturnType returnType() {
                return type;
            }
            
            @Override
            public T apply(ImplementationArguments implementationArguments, Scope scope) {
                return function.apply((TerraImplementationArguments) implementationArguments);
            }
            
            @Override
            public Position getPosition() {
                return position;
            }
        };
    }
    
    @Override
    public int argNumber() {
        return 0;
    }
    
    @Override
    public Returnable.ReturnType getArgument(int position) {
        if(position == 0) return type;
        return null;
    }
}
