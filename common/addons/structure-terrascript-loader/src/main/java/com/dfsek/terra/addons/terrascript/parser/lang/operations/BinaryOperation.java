package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.api.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.api.Position;

import java.util.Map;

public abstract class BinaryOperation<I, O> implements Returnable<O> {
    private final Returnable<I> left;
    private final Returnable<I> right;
    private final Position start;

    public BinaryOperation(Returnable<I> left, Returnable<I> right, Position start) {
        this.left = left;
        this.right = right;
        this.start = start;
    }

    public abstract O apply(I left, I right);

    @Override
    public Position getPosition() {
        return start;
    }

    @Override
    public O apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        return apply(left.apply(implementationArguments, variableMap), right.apply(implementationArguments, variableMap));
    }
}
