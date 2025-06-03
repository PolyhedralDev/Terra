/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.seismic.algorithms.sampler.normalizer.LinearNormalizer;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.seismic.type.sampler.Sampler;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class LinearNormalizerTemplate extends NormalizerTemplate<LinearNormalizer> {
    @Value("max")
    private @Meta double max;

    @Value("min")
    private @Meta double min;

    @Override
    public Sampler get() {
        return new LinearNormalizer(function, min, max);
    }
}
