package com.dfsek.terra.addons.structure.structures.parser.lang.constants;

import com.dfsek.terra.addons.structure.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.structure.structures.parser.lang.Returnable;
import com.dfsek.terra.addons.structure.structures.parser.lang.variables.Variable;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

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
