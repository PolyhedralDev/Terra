package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Random;

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
    public Number apply(Buffer buffer, Rotation rotation, Random random, int recursions) {
        return recursions;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
