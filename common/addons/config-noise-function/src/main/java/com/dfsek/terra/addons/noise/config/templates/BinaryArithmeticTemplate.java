package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.seismic.algorithms.sampler.arithmetic.BinaryArithmeticSampler;
import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.function.BiFunction;

import com.dfsek.terra.api.config.meta.Meta;


public class BinaryArithmeticTemplate<T extends BinaryArithmeticSampler> extends SamplerTemplate<T> {
    private final BiFunction<Sampler, Sampler, T> function;
    @Value("left")
    private @Meta Sampler left;
    @Value("right")
    private @Meta Sampler right;

    public BinaryArithmeticTemplate(BiFunction<Sampler, Sampler, T> function) {
        this.function = function;
    }

    @Override
    public T get() {
        return function.apply(left, right);
    }
}
