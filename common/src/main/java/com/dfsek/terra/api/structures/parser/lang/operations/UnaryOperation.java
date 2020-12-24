package com.dfsek.terra.api.structures.parser.lang.operations;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.tokenizer.Position;

public abstract class UnaryOperation<T> implements Returnable<T> {
    private final Returnable<T> input;
    private final Position position;

    public UnaryOperation(Returnable<T> input, Position position) {
        this.input = input;
        this.position = position;
    }

    public abstract T apply(T input);

    @Override
    public T apply(Location location, Rotation rotation, int recursions) {
        return apply(input.apply(location, rotation, recursions));
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
