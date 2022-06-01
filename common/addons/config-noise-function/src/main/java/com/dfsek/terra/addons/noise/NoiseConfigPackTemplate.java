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
import com.dfsek.terra.api.properties.Properties;


@SuppressWarnings("FieldMayBeFinal")
public class NoiseConfigPackTemplate implements ConfigTemplate, Properties {
    @Value("samplers")
    @Default
    private @Meta Map<String, @Meta DimensionApplicableNoiseSampler> noiseBuilderMap = new LinkedHashMap<>();
    
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
