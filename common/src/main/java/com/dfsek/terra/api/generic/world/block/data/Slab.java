package com.dfsek.terra.api.generic.world.block.data;

public interface Slab extends Waterlogged {
    Type getType();

    void setType(Type type);

    enum Type {
        TOP, BOTTOM, DOUBLE
    }
}
