package com.dfsek.terra.api.structures.parser.lang.variables;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class Assignment<T> implements Item<T> {
    private final Variable<T> delegate;
    private final Returnable<T> value;
    private final Position position;

    public Assignment(Variable<T> delegate, Returnable<T> value, Position position) {
        this.delegate = delegate;
        this.value = value;
        this.position = position;
    }

    @Override
    public T apply(Location location, Rotation rotation, int recursions) {
        T val = value.apply(location, rotation, recursions);
        delegate.setValue(val);
        return val;
    }

    @Override
    public T apply(Location location, Chunk chunk, Rotation rotation, int recursions) {
        T val = value.apply(location, chunk, rotation, recursions);
        delegate.setValue(val);
        return val;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
