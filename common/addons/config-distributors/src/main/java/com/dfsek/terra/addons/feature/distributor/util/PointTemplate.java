package com.dfsek.terra.addons.feature.distributor.util;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

public class PointTemplate implements ObjectTemplate<Point> {
    @Value("x")
    private int x;

    @Value("z")
    private int z;

    @Override
    public Point get() {
        return new Point(x, z);
    }
}
