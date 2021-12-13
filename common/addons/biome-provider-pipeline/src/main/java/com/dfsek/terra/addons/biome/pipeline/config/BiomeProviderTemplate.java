/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public abstract class BiomeProviderTemplate implements ObjectTemplate<BiomeProvider> {
    @Value("resolution")
    @Default
    protected @Meta int resolution = 1;
    @Value("blend.noise")
    @Default
    protected @Meta NoiseSampler blend = NoiseSampler.zero();
    @Value("blend.amplitude")
    @Default
    protected @Meta double blendAmp = 0d;
}
