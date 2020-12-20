package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;

public class ConstantExpression implements Expression<Object> {
    private final Object constant;

    public ConstantExpression(Object constant) {
        this.constant = constant;
    }

    @Override
    public Object apply(Location location) {
        return constant;
    }

    @Override
    public Object apply(Location location, Chunk chunk) {
        return constant;
    }
}
