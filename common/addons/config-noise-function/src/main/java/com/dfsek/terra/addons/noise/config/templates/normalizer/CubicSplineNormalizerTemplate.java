package com.dfsek.terra.addons.noise.config.templates.normalizer;


import com.dfsek.seismic.algorithms.sampler.normalizer.CubicSplineNormalizer;
import com.dfsek.seismic.type.CubicSpline;
import com.dfsek.seismic.type.CubicSpline.Point;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.List;

import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.seismic.type.sampler.Sampler;

public class CubicSplineNormalizerTemplate extends NormalizerTemplate<CubicSplineNormalizer> {

    @Value("points")
    private @Meta List<@Meta Point> points;

    @Override
    public Sampler get() {
        return new CubicSplineNormalizer(function, new CubicSpline(points));
    }
}
