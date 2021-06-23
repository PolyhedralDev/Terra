package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Map;

public class RecursionsFunction implements Function<Number> {
    private final Position position;

    public RecursionsFunction(Position position) {
        this.position = position;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }

    @Override
    public Number apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        return ((TerraImplementationArguments) implementationArguments).getRecursions();
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
