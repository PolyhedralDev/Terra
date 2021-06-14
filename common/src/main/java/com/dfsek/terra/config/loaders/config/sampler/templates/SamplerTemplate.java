package com.dfsek.terra.config.loaders.config.sampler.templates;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

@SuppressWarnings("FieldMayBeFinal")
public abstract class SamplerTemplate<T extends NoiseSampler> implements ValidatedConfigTemplate, ObjectTemplate<NoiseSeeded>, NoiseSeeded {
    @Value("dimensions")
    @Default
    private MetaValue<Integer> dimensions = MetaValue.of(2);

    public int getDimensions() {
        return dimensions.get();
    }

    @Override
    public boolean validate() throws ValidationException {
        if(dimensions.get() != 2 && dimensions.get() != 3) throw new ValidationException("Illegal amount of dimensions: " + dimensions);
        return true;
    }

    @Override
    public NoiseSeeded get() {
        return this;
    }
}
