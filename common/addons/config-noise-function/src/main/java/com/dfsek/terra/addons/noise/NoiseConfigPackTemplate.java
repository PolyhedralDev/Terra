/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.LinkedHashMap;
import java.util.Map;

import com.dfsek.terra.addons.noise.config.DimensionApplicableNoiseSampler;
import com.dfsek.terra.addons.noise.config.templates.FunctionTemplate;
import com.dfsek.terra.api.config.meta.Meta;


@SuppressWarnings("FieldMayBeFinal")
public class NoiseConfigPackTemplate implements ConfigTemplate {
    @Value("samplers")
    private @Meta Map<String, @Meta DimensionApplicableNoiseSampler> noiseBuilderMap;
    
    @Value("functions")
    @Default
    private @Meta LinkedHashMap<String, @Meta FunctionTemplate> expressions = new LinkedHashMap<>();
    
    public Map<String, DimensionApplicableNoiseSampler> getSamplers() {
        return noiseBuilderMap;
    }
    
    public LinkedHashMap<String, FunctionTemplate> getFunctions() {
        return expressions;
    }
}
