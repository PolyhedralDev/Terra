/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates;

import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.exception.ValidationException;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings("FieldMayBeFinal")
public abstract class SamplerTemplate<T extends NoiseSampler> implements ValidatedConfigTemplate, ObjectTemplate<NoiseSampler> {
    @Value("dimensions")
    @Default
    private @Meta int dimensions = 2;
    
    @Override
    public boolean validate() throws ValidationException {
        if(dimensions != 2 && dimensions != 3) throw new ValidationException("Illegal amount of dimensions: " + dimensions);
        return true;
    }
    
    public int getDimensions() {
        return dimensions;
    }
}
