package com.dfsek.terra.addons.noise.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.noise.math.CubicSpline.Point;
import com.dfsek.terra.api.config.meta.Meta;


public class CubicSplinePointTemplate implements ObjectTemplate<Point> {

    @Value("from")
    private @Meta double from;

    @Value("to")
    private @Meta double to;

    @Value("gradient")
    private @Meta double gradient;

    @Override
    public Point get() {
        return new Point(from, to, gradient);
    }
}
