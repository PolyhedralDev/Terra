package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.function.BiFunction;

import com.dfsek.terra.addons.noise.samplers.arithmetic.BinaryArithmeticSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


public class BinaryArithmeticTemplate<T extends BinaryArithmeticSampler> extends SamplerTemplate<T> {
    private final BiFunction<NoiseSampler, NoiseSampler, T> function;
    @Value("left")
    private @Meta NoiseSampler left;
    @Value("right")
    private @Meta NoiseSampler right;
    
    public BinaryArithmeticTemplate(BiFunction<NoiseSampler, NoiseSampler, T> function) {
        this.function = function;
    }
    
    @Override
    public T get() {
        return function.apply(left, right);
    }
}
