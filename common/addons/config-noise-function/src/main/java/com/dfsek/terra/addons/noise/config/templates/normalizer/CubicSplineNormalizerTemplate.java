package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.math.CubicSpline;
import com.dfsek.terra.addons.noise.math.CubicSpline.Point;
import com.dfsek.terra.addons.noise.normalizer.CubicSplineNoiseSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;

import java.util.List;


public class CubicSplineNormalizerTemplate extends NormalizerTemplate<CubicSplineNoiseSampler> {
    
    @Value("points")
    private @Meta List<@Meta Point> points;
    
    @Override
    public NoiseSampler get() {
        return new CubicSplineNoiseSampler(function, new CubicSpline(points));
    }
}
