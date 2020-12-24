package com.dfsek.terra.api.structures.parser.lang.functions.builtin;

import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.tokenizer.Position;

public abstract class MathFunction implements Function<Number> {
    private final Position position;

    protected MathFunction(Position position) {
        this.position = position;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
