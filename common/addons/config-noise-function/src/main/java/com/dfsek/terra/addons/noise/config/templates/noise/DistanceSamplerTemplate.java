package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.samplers.noise.DistanceSampler;
import com.dfsek.terra.addons.noise.samplers.noise.DistanceSampler.DistanceFunction;
import com.dfsek.terra.api.config.meta.Meta;


public class DistanceSamplerTemplate extends SamplerTemplate<DistanceSampler> {

    @Value("distance-function")
    @Default
    private DistanceSampler.@Meta DistanceFunction distanceFunction = DistanceFunction.Euclidean;

    @Value("point.x")
    @Default
    private @Meta double x = 0;

    @Value("point.y")
    @Default
    private @Meta double y = 0;

    @Value("point.z")
    @Default
    private @Meta double z = 0;

    @Value("normalize")
    @Default
    private @Meta boolean normalize = false;

    @Value("radius")
    @Default
    private @Meta double normalizeRadius = 100;

    @Override
    public DistanceSampler get() {
        return new DistanceSampler(distanceFunction, x, y, z, normalize, normalizeRadius);
    }
}
