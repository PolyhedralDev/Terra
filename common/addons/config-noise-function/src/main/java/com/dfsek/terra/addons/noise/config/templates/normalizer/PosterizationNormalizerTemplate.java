/*
 * Copyright (c) 2022 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.seismic.algorithms.sampler.normalizer.PosterizationNormalizer;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.seismic.type.sampler.Sampler;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class PosterizationNormalizerTemplate extends NormalizerTemplate<PosterizationNormalizer> {
    @Value("steps")
    private @Meta int steps;

    @Override
    public Sampler get() {
        return new PosterizationNormalizer(function, steps);
    }
}
