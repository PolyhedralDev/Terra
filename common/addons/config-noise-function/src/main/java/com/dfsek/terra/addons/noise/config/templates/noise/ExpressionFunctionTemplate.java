/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.paralithic.functions.Function;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.dfsek.terra.addons.noise.config.DimensionApplicableNoiseSampler;
import com.dfsek.terra.addons.noise.config.templates.FunctionTemplate;
import com.dfsek.terra.addons.noise.config.templates.SamplerTemplate;
import com.dfsek.terra.addons.noise.paralithic.defined.UserDefinedFunction;
import com.dfsek.terra.addons.noise.paralithic.noise.NoiseFunction2;
import com.dfsek.terra.addons.noise.paralithic.noise.NoiseFunction3;
import com.dfsek.terra.addons.noise.samplers.noise.ExpressionFunction;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class ExpressionFunctionTemplate extends SamplerTemplate<ExpressionFunction> {
    private final Map<String, DimensionApplicableNoiseSampler> otherFunctions;
    private final Map<String, FunctionTemplate> globalFunctions;
    @Value("variables")
    @Default
    private @Meta Map<String, @Meta Double> vars = new HashMap<>();
    @Value("expression")
    private @Meta String expression;
    @Value("samplers")
    @Default
    private @Meta LinkedHashMap<String, @Meta DimensionApplicableNoiseSampler> samplers = new LinkedHashMap<>();
    @Value("functions")
    @Default
    private @Meta LinkedHashMap<String, @Meta FunctionTemplate> functions = new LinkedHashMap<>();
    
    public ExpressionFunctionTemplate(Map<String, DimensionApplicableNoiseSampler> otherFunctions, Map<String, FunctionTemplate> samplers) {
        this.otherFunctions = otherFunctions;
        this.globalFunctions = samplers;
    }
    
    @Override
    public NoiseSampler get() {
        try {
            Map<String, Function> noiseFunctionMap = generateFunctions();
            return new ExpressionFunction(noiseFunctionMap, expression, vars);
        } catch(ParseException e) {
            throw new RuntimeException("Failed to parse expression.", e);
        }
    }
    
    private Map<String, Function> generateFunctions() throws ParseException {
        Map<String, Function> noiseFunctionMap = new HashMap<>();
        
        for(Map.Entry<String, FunctionTemplate> entry : globalFunctions.entrySet()) {
            noiseFunctionMap.put(entry.getKey(), UserDefinedFunction.newInstance(entry.getValue()));
        }
        
        for(Map.Entry<String, FunctionTemplate> entry : functions.entrySet()) {
            noiseFunctionMap.put(entry.getKey(), UserDefinedFunction.newInstance(entry.getValue()));
        }
        
        otherFunctions.forEach((id, function) -> {
            if(function.getDimensions() == 2) {
                noiseFunctionMap.put(id, new NoiseFunction2(function.getSampler()));
            } else noiseFunctionMap.put(id, new NoiseFunction3(function.getSampler()));
        });
        
        samplers.forEach((id, function) -> {
            if(function.getDimensions() == 2) {
                noiseFunctionMap.put(id, new NoiseFunction2(function.getSampler()));
            } else noiseFunctionMap.put(id, new NoiseFunction3(function.getSampler()));
        });
        
        return noiseFunctionMap;
    }
}
