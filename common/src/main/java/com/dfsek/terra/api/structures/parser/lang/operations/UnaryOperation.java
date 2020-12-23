package com.dfsek.terra.api.structures.parser.lang.operations;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.structure.Rotation;

public abstract class UnaryOperation<T> implements Returnable<T> {
    private final Returnable<T> input;
    private final Position position;

    public UnaryOperation(Returnable<T> input, Position position) {
        this.input = input;
        this.position = position;
    }

    public abstract T apply(T input);

    @Override
    public T apply(Location location, Rotation rotation) {
        return apply(input.apply(location, rotation));
    }

    @Override
    public T apply(Location location, Chunk chunk, Rotation rotation) {
        return apply(input.apply(location, chunk, rotation));
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
