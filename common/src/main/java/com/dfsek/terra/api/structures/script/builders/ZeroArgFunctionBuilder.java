package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;

public class ZeroArgFunctionBuilder<T> implements FunctionBuilder<Function<T>> {
    private final java.util.function.Function<TerraImplementationArguments, T> function;
    private final Returnable.ReturnType type;

    public ZeroArgFunctionBuilder(java.util.function.Function<TerraImplementationArguments, T> function, Returnable.ReturnType type) {
        this.function = function;
        this.type = type;
    }

    @Override
    public Function<T> build(List<Returnable<?>> argumentList, Position position) {
        return new Function<T>() {
            @Override
            public ReturnType returnType() {
                return type;
            }

            @Override
            public T apply(ImplementationArguments implementationArguments) {
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
        return 1;
    }

    @Override
    public Returnable.ReturnType getArgument(int position) {
        if(position == 0) return type;
        return null;
    }
}
