package com.dfsek.terra.biome.pipeline;

import com.dfsek.terra.api.math.vector.Vector2;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2 getAsVector() {
        return new Vector2(x, y);
    }
}
