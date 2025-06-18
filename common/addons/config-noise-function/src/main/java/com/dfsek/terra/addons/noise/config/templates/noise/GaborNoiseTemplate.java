/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.seismic.algorithms.sampler.noise.GaborSampler;
import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;


@SuppressWarnings("FieldMayBeFinal")
public class GaborNoiseTemplate extends NoiseTemplate<GaborSampler> {
    @Value("rotation")
    @Default
    private @Meta double rotation = 0.25;

    @Value("isotropic")
    @Default
    private @Meta boolean isotropic = true;

    @Value("deviation")
    @Default
    private @Meta double deviation = 1.0;

    @Value("impulses")
    @Default
    private @Meta double impulses = 64d;

    @Value("frequency_0")
    @Default
    private @Meta double f0 = 0.625;

    @Override
    public Sampler get() {
        GaborSampler gaborSampler = new GaborSampler();
        gaborSampler.setFrequency(frequency);
        gaborSampler.setRotation(rotation);
        gaborSampler.setIsotropic(isotropic);
        gaborSampler.setDeviation(deviation);
        gaborSampler.setImpulsesPerKernel(impulses);
        gaborSampler.setFrequency0(f0);
        gaborSampler.setSalt(salt);
        return gaborSampler;
    }
}
