package com.dfsek.terra.addons.terrascript.api.lang;

import com.dfsek.terra.addons.terrascript.api.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;

import java.util.Map;

public abstract class ConstantExpression<T> implements Returnable<T> {
    private final T constant;
    private final Position position;

    public ConstantExpression(T constant, Position position) {
        this.constant = constant;
        this.position = position;
    }

    @Override
    public T apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        return constant;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public T getConstant() {
        return constant;
    }
}
