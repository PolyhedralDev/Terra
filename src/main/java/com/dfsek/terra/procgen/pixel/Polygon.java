package com.dfsek.terra.procgen.pixel;

import com.dfsek.terra.api.generic.world.vector.Vector2;

import java.util.Set;

@SuppressWarnings("unused")
public abstract class Polygon {
    public abstract Set<Vector2> getContainedPixels();
}
