package com.dfsek.terra.api.structures.parser.lang.variables;

import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Random;

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
    public synchronized T apply(Buffer buffer, Rotation rotation, Random random, int recursions) {
        T val = value.apply(buffer, rotation, random, recursions);
        delegate.setValue(val);
        return val;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
