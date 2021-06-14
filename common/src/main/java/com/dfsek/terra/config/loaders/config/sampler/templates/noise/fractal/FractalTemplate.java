package com.dfsek.terra.config.loaders.config.sampler.templates.noise.fractal;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.noise.samplers.noise.fractal.FractalNoiseFunction;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.sampler.templates.SamplerTemplate;

public abstract class FractalTemplate<T extends FractalNoiseFunction> extends SamplerTemplate<T> {
    @Value("octaves")
    @Default
    protected MetaValue<Integer> octaves = MetaValue.of(3);

    @Value("gain")
    @Default
    protected MetaValue<Double> fractalGain = MetaValue.of(0.5D);

    @Value("lacunarity")
    @Default
    protected MetaValue<Double> fractalLacunarity = MetaValue.of(2.0D);

    @Value("weighted-strength")
    @Default
    protected MetaValue<Double> weightedStrength = MetaValue.of(0.0D);

    @Value("function")
    protected MetaValue<NoiseSeeded> function;
}
