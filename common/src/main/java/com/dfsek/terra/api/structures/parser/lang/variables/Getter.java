package com.dfsek.terra.api.structures.parser.lang.variables;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class Getter implements Returnable<Object> {
    private final Variable<?> delegate;

    public Getter(Variable<?> delegate) {
        this.delegate = delegate;
    }

    @Override
    public ReturnType returnType() {
        return delegate.getType();
    }

    @Override
    public synchronized Object apply(ImplementationArguments implementationArguments) {
        return delegate.getValue();
    }

    @Override
    public Position getPosition() {
        return delegate.getPosition();
    }
}
