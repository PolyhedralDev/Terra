package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;

public class ConstantExpression<T> implements Executable<T> {
    private final T constant;

    public ConstantExpression(T constant) {
        this.constant = constant;
    }

    @Override
    public T apply(Location location) {
        return constant;
    }


    @Override
    public T apply(Location location, Chunk chunk) {
        return constant;
    }

    @Override
    public ReturnType returnType() {
        if(constant instanceof String) return ReturnType.STRING;
        if(constant instanceof Number) return ReturnType.NUMBER;
        if(constant instanceof Boolean) return ReturnType.BOOLEAN;
        return ReturnType.OBJECT;
    }
}
