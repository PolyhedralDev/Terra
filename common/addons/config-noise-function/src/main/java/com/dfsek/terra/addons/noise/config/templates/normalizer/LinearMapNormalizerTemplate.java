package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.seismic.algorithms.sampler.normalizer.LinearMapNormalizer;
import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;


public class LinearMapNormalizerTemplate extends NormalizerTemplate<LinearMapNormalizer> {

    @Value("from.a")
    @Default
    private @Meta double aFrom = -1;

    @Value("from.b")
    @Default
    private @Meta double bFrom = 1;

    @Value("to.a")
    private @Meta double aTo;

    @Value("to.b")
    private @Meta double bTo;

    @Override
    public Sampler get() {
        return new LinearMapNormalizer(function, aFrom, aTo, bFrom, bTo);
    }
}
