package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.samplers.arithmetic.BinaryArithmeticSampler;
import com.dfsek.terra.api.noise.NoiseSampler;

import java.util.function.BiFunction;


public class BinaryArithmeticTemplate<T extends BinaryArithmeticSampler> extends SamplerTemplate<T> {
    private final BiFunction<NoiseSampler, NoiseSampler, T> function;
    @Value("left")
    private NoiseSampler left;
    @Value("right")
    private NoiseSampler right;
    
    public BinaryArithmeticTemplate(BiFunction<NoiseSampler, NoiseSampler, T> function) {
        this.function = function;
    }
    
    @Override
    public T get() {
        return function.apply(left, right);
    }
}
