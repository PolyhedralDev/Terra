/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.samplers.noise.random.WhiteNoiseSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.addons.noise.samplers.noise.PseudoErosionSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings("FieldMayBeFinal")
public class PseudoErosionSamplerTemplate extends SamplerTemplate<PseudoErosionSampler> {
    
    @Value("frequency")
    @Default
    protected @Meta double frequency = 0.01d;
//    protected @Meta double frequency = 0.02d;
    
    @Value("salt")
    @Default
    protected @Meta long salt = 0;
    
    @Value("jitter")
    @Default
    private @Meta double jitter = 1.0D;
    
    @Value("lookup")
    @Default
    private @Meta NoiseSampler lookup = null;
    
    @Override
    public NoiseSampler get() {
        if(lookup == null) {
//            OpenSimplex2Sampler l = new OpenSimplex2Sampler();
//            l.setFrequency(0.0005);
            lookup = new WhiteNoiseSampler();
        }
        return new PseudoErosionSampler(salt, frequency, lookup, jitter);
    }
}
