package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;
import java.util.function.BiFunction;

public class BinaryNumberFunctionBuilder implements FunctionBuilder<Function<Number>> {

    private final BiFunction<Number, Number, Number> function;

    public BinaryNumberFunctionBuilder(BiFunction<Number, Number, Number> function) {
        this.function = function;
    }

    @Override
    public Function<Number> build(List<Returnable<?>> argumentList, Position position) {
        return new Function<Number>() {
            @Override
            public ReturnType returnType() {
                return ReturnType.NUMBER;
            }

            @SuppressWarnings("unchecked")
            @Override
            public Number apply(ImplementationArguments implementationArguments) {
                return function.apply(((Returnable<Number>) argumentList.get(0)).apply(implementationArguments), ((Returnable<Number>) argumentList.get(1)).apply(implementationArguments));
            }

            @Override
            public Position getPosition() {
                return position;
            }
        };
    }

    @Override
    public int argNumber() {
        return 2;
    }

    @Override
    public Returnable.ReturnType getArgument(int position) {
        if(position == 0 || position == 1) return Returnable.ReturnType.NUMBER;
        return null;
    }
}
