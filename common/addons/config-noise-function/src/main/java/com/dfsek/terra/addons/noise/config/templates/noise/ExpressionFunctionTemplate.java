/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.paralithic.eval.parser.Parser.ParseOptions;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.paralithic.sampler.noise.ExpressionNoiseFunction;
import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.dfsek.terra.addons.noise.config.DimensionApplicableSampler;
import com.dfsek.terra.addons.noise.config.templates.FunctionTemplate;
import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.api.config.meta.Meta;

import static com.dfsek.terra.addons.noise.paralithic.FunctionUtil.convertFunctionsAndSamplers;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class ExpressionFunctionTemplate extends SamplerTemplate<ExpressionNoiseFunction> {
    private final Map<String, DimensionApplicableSampler> globalSamplers;
    private final Map<String, FunctionTemplate> globalFunctions;
    private final ParseOptions parseOptions;
    @Value("variables")
    @Default
    private @Meta Map<String, @Meta Double> vars = new HashMap<>();
    @Value("expression")
    private @Meta String expression;
    @Value("samplers")
    @Default
    private @Meta LinkedHashMap<String, @Meta DimensionApplicableSampler> samplers = new LinkedHashMap<>();
    @Value("functions")
    @Default
    private @Meta LinkedHashMap<String, @Meta FunctionTemplate> functions = new LinkedHashMap<>();

    public ExpressionFunctionTemplate(Map<String, DimensionApplicableSampler> globalSamplers,
                                      Map<String, FunctionTemplate> globalFunctions,
                                      ParseOptions parseOptions) {
        this.globalSamplers = globalSamplers;
        this.globalFunctions = globalFunctions;
        this.parseOptions = parseOptions;
    }

    @Override
    public Sampler get() {
        var mergedFunctions = new HashMap<>(globalFunctions);
        mergedFunctions.putAll(functions);
        var mergedSamplers = new HashMap<>(globalSamplers);
        mergedSamplers.putAll(samplers);
        try {
            return new ExpressionNoiseFunction(convertFunctionsAndSamplers(mergedFunctions, mergedSamplers), expression, vars,
                parseOptions);
        } catch(ParseException e) {
            throw new RuntimeException("Failed to parse expression.", e);
        }
    }
}
