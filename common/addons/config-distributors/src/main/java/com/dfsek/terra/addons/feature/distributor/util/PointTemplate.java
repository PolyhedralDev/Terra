package com.dfsek.terra.addons.feature.distributor.util;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import com.dfsek.terra.api.config.meta.Meta;


public class PointTemplate implements ObjectTemplate<Point> {
    @Value("x")
    private @Meta int x;
    
    @Value("z")
    private @Meta int z;
    
    @Override
    public Point get() {
        return new Point(x, z);
    }
}
