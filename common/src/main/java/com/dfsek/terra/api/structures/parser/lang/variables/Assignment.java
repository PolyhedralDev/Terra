package com.dfsek.terra.api.structures.parser.lang.variables;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
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
    public synchronized T apply(ImplementationArguments implementationArguments) {
        T val = value.apply(implementationArguments);
        delegate.setValue(val);
        return val;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
