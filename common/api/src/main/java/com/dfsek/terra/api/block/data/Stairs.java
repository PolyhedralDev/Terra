package com.dfsek.terra.api.block.data;

public interface Stairs extends Waterlogged, Directional, Bisected {
    Shape getShape();

    void setShape(Shape shape);

    enum Shape {
        /**
         * Regular stair block.
         */
        STRAIGHT,
        /**
         * Inner corner stair block with higher left side.
         */
        INNER_LEFT,
        /**
         * Inner corner stair block with higher right side.
         */
        INNER_RIGHT,
        /**
         * Outer corner stair block with higher left side.
         */
        OUTER_LEFT,
        /**
         * Outer corner stair block with higher right side.
         */
        OUTER_RIGHT
    }
}
