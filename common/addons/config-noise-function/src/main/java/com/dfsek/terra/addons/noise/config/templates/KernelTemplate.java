/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.exception.ValidationException;

import java.util.List;

import com.dfsek.terra.addons.noise.samplers.KernelSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class KernelTemplate extends SamplerTemplate<KernelSampler> {
    
    @Value("kernel")
    private @Meta List<@Meta List<@Meta Double>> kernel;
    
    @Value("factor")
    @Default
    private @Meta double factor = 1;
    
    @Value("sampler")
    private @Meta NoiseSampler function;
    
    @Value("frequency")
    @Default
    private @Meta double frequency = 1;
    
    @Override
    public NoiseSampler get() {
        double[][] k = new double[kernel.size()][kernel.get(0).size()];
        
        for(int x = 0; x < kernel.size(); x++) {
            for(int y = 0; y < kernel.get(x).size(); y++) {
                k[x][y] = kernel.get(x).get(y) * factor;
            }
        }
        
        KernelSampler sampler = new KernelSampler(k, function);
        sampler.setFrequency(frequency);
        return sampler;
    }
    
    @Override
    public boolean validate() throws ValidationException {
        
        if(kernel.isEmpty()) throw new ValidationException("Kernel must not be empty.");
        
        int len = kernel.get(0).size();
        
        if(len == 0) throw new ValidationException("Kernel row must contain data.");
        
        for(int i = 0; i < kernel.size(); i++) {
            if(kernel.get(i).size() != len)
                throw new ValidationException("Kernel row " + i + " size mismatch. Expected " + len + ", found " + kernel.get(i).size());
        }
        
        return super.validate();
    }
}
