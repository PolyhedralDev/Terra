package com.dfsek.terra.api.math.pixel;

import com.dfsek.terra.api.math.vector.Vector2;

import java.util.Set;

@SuppressWarnings("unused")
public abstract class Polygon {
    public abstract Set<Vector2> getContainedPixels();
}
