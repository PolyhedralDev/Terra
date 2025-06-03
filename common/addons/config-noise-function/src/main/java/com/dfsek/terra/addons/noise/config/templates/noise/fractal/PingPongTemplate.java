/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise.fractal;

import com.dfsek.seismic.algorithms.sampler.noise.fractal.PingPongSampler;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.seismic.type.sampler.Sampler;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class PingPongTemplate extends FractalTemplate<PingPongSampler> {
    @Value("ping-pong")
    @Default
    private @Meta double pingPong = 2.0D;

    @Override
    public Sampler get() {
        PingPongSampler sampler = new PingPongSampler(function);
        sampler.setGain(fractalGain);
        sampler.setLacunarity(fractalLacunarity);
        sampler.setOctaves(octaves);
        sampler.setWeightedStrength(weightedStrength);
        sampler.setPingPongStrength(pingPong);
        return sampler;
    }
}
