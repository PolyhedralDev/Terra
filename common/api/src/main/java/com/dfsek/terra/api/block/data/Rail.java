package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.BlockState;

public interface Rail extends BlockState {
    Shape getShape();

    void setShape(Shape newShape);

    enum Shape {
        ASCENDING_EAST,
        ASCENDING_NORTH,
        ASCENDING_SOUTH,
        ASCENDING_WEST,
        EAST_WEST,
        NORTH_EAST,
        NORTH_SOUTH,
        NORTH_WEST,
        SOUTH_EAST,
        SOUTH_WEST
    }
}
