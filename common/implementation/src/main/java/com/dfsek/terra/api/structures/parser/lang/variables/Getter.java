package com.dfsek.terra.api.structures.parser.lang.variables;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Map;

public class Getter implements Returnable<Object> {
    private final String identifier;
    private final Position position;
    private final ReturnType type;

    public Getter(String identifier, Position position, ReturnType type) {
        this.identifier = identifier;
        this.position = position;
        this.type = type;
    }

    @Override
    public ReturnType returnType() {
        return type;
    }

    @Override
    public synchronized Object apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        return variableMap.get(identifier).getValue();
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
