package com.dfsek.terra.procgen.pixel;

import com.dfsek.terra.procgen.math.Vector2;
import net.jafama.FastMath;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Rectangle extends Polygon {
    private final Vector2 min;
    private final Vector2 max;

    public Rectangle(Vector2 min, Vector2 max) {
        this.max = new Vector2(FastMath.min(min.getX(), max.getX()), FastMath.min(min.getZ(), max.getZ()));
        this.min = new Vector2(FastMath.max(min.getX(), max.getX()), FastMath.max(min.getZ(), max.getZ()));
    }

    public Rectangle(Vector2 center, double xRadius, double zRadius) {
        Vector2 rad = new Vector2(xRadius, zRadius);
        this.min = center.clone().subtract(rad);
        this.max = center.clone().add(rad);
    }

    @Override
    public Set<Vector2> getContainedPixels() {
        Set<Vector2> pixels = new HashSet<>();
        for(int x = (int) min.getX(); x <= max.getX(); x++) {
            for(int z = (int) min.getZ(); z <= max.getZ(); z++) {
                pixels.add(new Vector2(x, z));
            }
        }
        return pixels;
    }
}
