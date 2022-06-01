/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.samplers.noise.CellularSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings("FieldMayBeFinal")
public class CellularNoiseTemplate extends NoiseTemplate<CellularSampler> {
    @Value("distance")
    @Default
    private CellularSampler.@Meta DistanceFunction cellularDistanceFunction = CellularSampler.DistanceFunction.EuclideanSq;
    
    @Value("return")
    @Default
    private CellularSampler.@Meta ReturnType cellularReturnType = CellularSampler.ReturnType.Distance;
    
    @Value("jitter")
    @Default
    private @Meta double cellularJitter = 1.0D;
    
    
    @Value("lookup")
    @Default
    private @Meta NoiseSampler lookup = new OpenSimplex2Sampler();
    
    @Override
    public NoiseSampler get() {
        CellularSampler sampler = new CellularSampler();
        sampler.setNoiseLookup(lookup);
        sampler.setFrequency(frequency);
        sampler.setJitterModifier(cellularJitter);
        sampler.setReturnType(cellularReturnType);
        sampler.setDistanceFunction(cellularDistanceFunction);
        sampler.setSalt(salt);
        return sampler;
    }
}
