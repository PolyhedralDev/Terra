package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.addons.noise.samplers.KernelSampler;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;

import java.util.List;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class KernelTemplate extends SamplerTemplate<KernelSampler> implements ValidatedConfigTemplate {

    @Value("kernel")
    private List<List<Double>> kernel;

    @Value("factor")
    @Default
    private double factor = 1;

    @Value("function")
    private NoiseSampler function;

    @Value("frequency")
    @Default
    private double frequency = 1;

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
