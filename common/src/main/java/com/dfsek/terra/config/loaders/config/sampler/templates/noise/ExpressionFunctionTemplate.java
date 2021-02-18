package com.dfsek.terra.config.loaders.config.sampler.templates.noise;

import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.paralithic.functions.Function;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.ExpressionFunction;
import com.dfsek.terra.api.math.parsii.BlankFunction;
import com.dfsek.terra.api.math.parsii.noise.NoiseFunction2;
import com.dfsek.terra.api.math.parsii.noise.NoiseFunction3;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.sampler.templates.SamplerTemplate;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class ExpressionFunctionTemplate extends SamplerTemplate<ExpressionFunction> implements ValidatedConfigTemplate {
    @Value("variables")
    @Default
    private Map<String, Double> vars = new HashMap<>();

    @Value("equation")
    private String equation;

    @Value("functions")
    private Map<String, NoiseSeeded> functions;

    @Override
    public NoiseSampler apply(Long seed) {
        try {
            Map<String, Function> noiseFunctionMap = new HashMap<>();

            functions.forEach((id, function) -> {
                if(function.getDimensions() == 2) {
                    noiseFunctionMap.put(id, new NoiseFunction2(function.apply(seed)));
                } else noiseFunctionMap.put(id, new NoiseFunction3(function.apply(seed)));
            });

            return new ExpressionFunction(noiseFunctionMap, equation, vars);
        } catch(ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean validate() throws ValidationException {
        try {
            Map<String, Function> noiseFunctionMap = new HashMap<>();

            functions.forEach((id, function) -> {
                if(function.getDimensions() == 2) {
                    noiseFunctionMap.put(id, new BlankFunction(2));
                } else noiseFunctionMap.put(id, new BlankFunction(3));
            });
            new ExpressionFunction(noiseFunctionMap, equation, vars);
        } catch(ParseException e) {
            throw new ValidationException("Errors occurred while parsing noise equation: ", e);
        }
        return super.validate();
    }
}
