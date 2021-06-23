package com.dfsek.terra.api.structures.parser.lang.operations;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Map;

public abstract class UnaryOperation<T> implements Returnable<T> {
    private final Returnable<T> input;
    private final Position position;

    public UnaryOperation(Returnable<T> input, Position position) {
        this.input = input;
        this.position = position;
    }

    public abstract T apply(T input);

    @Override
    public T apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        return apply(input.apply(implementationArguments, variableMap));
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
