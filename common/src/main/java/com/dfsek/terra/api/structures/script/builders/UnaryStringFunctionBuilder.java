package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;
import java.util.Map;

public class UnaryStringFunctionBuilder implements FunctionBuilder<Function<Void>> {

    private final java.util.function.Consumer<String> function;

    public UnaryStringFunctionBuilder(java.util.function.Consumer<String> function) {
        this.function = function;
    }

    @Override
    public Function<Void> build(List<Returnable<?>> argumentList, Position position) {
        return new Function<Void>() {
            @Override
            public ReturnType returnType() {
                return ReturnType.VOID;
            }

            @SuppressWarnings("unchecked")
            @Override
            public Void apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
                function.accept(((Returnable<String>) argumentList.get(0)).apply(implementationArguments, variableMap));
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
