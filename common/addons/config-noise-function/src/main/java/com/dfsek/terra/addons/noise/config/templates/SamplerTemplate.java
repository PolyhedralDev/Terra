package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

@SuppressWarnings("FieldMayBeFinal")
public abstract class SamplerTemplate<T extends NoiseSampler> implements ValidatedConfigTemplate, ObjectTemplate<NoiseSeeded>, NoiseSeeded {
    @Value("dimensions")
    @Default
    private int dimensions = 2;

    public int getDimensions() {
        return dimensions;
    }

    @Override
    public boolean validate() throws ValidationException {
        if(dimensions != 2 && dimensions != 3) throw new ValidationException("Illegal amount of dimensions: " + dimensions);
        return true;
    }

    @Override
    public NoiseSeeded get() {
        return this;
    }
}
