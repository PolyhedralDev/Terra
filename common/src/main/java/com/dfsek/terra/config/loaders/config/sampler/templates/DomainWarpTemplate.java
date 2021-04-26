package com.dfsek.terra.config.loaders.config.sampler.templates;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.docs.AutoDocAlias;
import com.dfsek.terra.api.math.noise.samplers.DomainWarpedSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

/**
 * Defines a domain-warped noise function.
 */
@SuppressWarnings({"unused", "FieldMayBeFinal"})
@AutoDocAlias("DomainWarpedSampler")
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
    public DomainWarpedSampler apply(Long seed) {
        return new DomainWarpedSampler(function.apply(seed), warp.apply(seed), (int) (seed + salt), amplitude);
    }
}
