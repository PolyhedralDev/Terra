package com.dfsek.terra.config.loaders.config.sampler.templates.noise;

import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.paralithic.functions.Function;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.noise.samplers.noise.ExpressionFunction;
import com.dfsek.terra.api.math.paralithic.defined.UserDefinedFunction;
import com.dfsek.terra.api.math.paralithic.noise.NoiseFunction2;
import com.dfsek.terra.api.math.paralithic.noise.NoiseFunction3;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;
import com.dfsek.terra.config.loaders.config.sampler.templates.SamplerTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class ExpressionFunctionTemplate extends SamplerTemplate<ExpressionFunction> implements ValidatedConfigTemplate {
    @Value("variables")
    @Default
    private Map<String, Double> vars = new HashMap<>();

    @Value("equation")
    private String equation;

    @Value("functions")
    @Default
    private LinkedHashMap<String, NoiseSeeded> functions = new LinkedHashMap<>();

    @Value("expressions")
    @Default
    private LinkedHashMap<String, FunctionTemplate> expressions = new LinkedHashMap<>();

    @Override
    public NoiseSampler apply(Long seed) {
        try {
            Map<String, Function> noiseFunctionMap = generateFunctions(seed);
            return new ExpressionFunction(noiseFunctionMap, equation, vars);
        } catch(ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean validate() throws ValidationException {
        try {
            Map<String, Function> noiseFunctionMap = generateFunctions(0L);
            new ExpressionFunction(noiseFunctionMap, equation, vars);
        } catch(ParseException e) {
            throw new ValidationException("Errors occurred while parsing noise equation: ", e);
        }
        return super.validate();
    }

    private Map<String, Function> generateFunctions(Long seed) throws ParseException {
        Map<String, Function> noiseFunctionMap = new HashMap<>();

        for(Map.Entry<String, FunctionTemplate> entry : expressions.entrySet()) {
            noiseFunctionMap.put(entry.getKey(), UserDefinedFunction.newInstance(entry.getValue(), new Parser(), new Scope()));
        }

        functions.forEach((id, function) -> {
            if(function.getDimensions() == 2) {
                noiseFunctionMap.put(id, new NoiseFunction2(function.apply(seed)));
            } else noiseFunctionMap.put(id, new NoiseFunction3(function.apply(seed)));
        });

        return noiseFunctionMap;
    }
}
