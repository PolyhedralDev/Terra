/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.normalizer;

import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.dfsek.terra.addons.noise.config.DimensionApplicableNoiseSampler;
import com.dfsek.terra.addons.noise.config.templates.FunctionTemplate;
import com.dfsek.terra.addons.noise.normalizer.ExpressionNormalizer;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;

import static com.dfsek.terra.addons.noise.paralithic.FunctionUtil.convertFunctionsAndSamplers;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class ExpressionNormalizerTemplate extends NormalizerTemplate<ExpressionNormalizer> {

    private final Map<String, DimensionApplicableNoiseSampler> globalSamplers;
    private final Map<String, FunctionTemplate> globalFunctions;

    @Value("expression")
    private @Meta String expression;

    @Value("variables")
    @Default
    private @Meta Map<String, @Meta Double> vars = new HashMap<>();

    @Value("samplers")
    @Default
    private @Meta LinkedHashMap<String, @Meta DimensionApplicableNoiseSampler> samplers = new LinkedHashMap<>();

    @Value("functions")
    @Default
    private @Meta LinkedHashMap<String, @Meta FunctionTemplate> functions = new LinkedHashMap<>();

    public ExpressionNormalizerTemplate(Map<String, DimensionApplicableNoiseSampler> globalSamplers,
                                        Map<String, FunctionTemplate> globalFunctions) {
        this.globalSamplers = globalSamplers;
        this.globalFunctions = globalFunctions;
    }

    @Override
    public NoiseSampler get() {
        var mergedFunctions = new HashMap<>(globalFunctions);
        mergedFunctions.putAll(functions);
        var mergedSamplers = new HashMap<>(globalSamplers);
        mergedSamplers.putAll(samplers);
        try {
            return new ExpressionNormalizer(function, convertFunctionsAndSamplers(mergedFunctions, mergedSamplers), expression, vars);
        } catch(ParseException e) {
            throw new RuntimeException("Failed to parse expression.", e);
        }
    }
}
