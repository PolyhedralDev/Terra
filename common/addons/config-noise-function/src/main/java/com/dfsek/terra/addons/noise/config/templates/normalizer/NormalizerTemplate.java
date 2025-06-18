/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.seismic.algorithms.sampler.normalizer.Normalizer;
import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.api.config.meta.Meta;


public abstract class NormalizerTemplate<T extends Normalizer> extends SamplerTemplate<T> {
    @Value("sampler")
    protected @Meta Sampler function;
}
