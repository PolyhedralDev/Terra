package com.dfsek.terra.config.loaders.config.sampler.templates;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.DomainWarpedSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

/**
 * Defines a domain-warped noise function.
 */
@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class DomainWarpTemplate extends SamplerTemplate<DomainWarpedSampler> {
    /**
     * Noise function used to warp input function.
     */
    @Value("warp")
    private NoiseSeeded warp;

    /**
     * Input function (function to be warped)
     */
    @Value("function")
    private NoiseSeeded function;

    /**
     * Salt for both warp function and
     * input function.
     */
    @Value("salt")
    @Default
    private int salt = 0;

    /**
     * Amplitude of warping. Values provided by
     * the warp function are multiplied by this constant.
     */
    @Value("amplitude")
    @Default
    private double amplitude = 1;

    @Override
    public NoiseSampler apply(Long seed) {
        return new DomainWarpedSampler(function.apply(seed), warp.apply(seed), (int) (seed + salt), amplitude);
    }
}
