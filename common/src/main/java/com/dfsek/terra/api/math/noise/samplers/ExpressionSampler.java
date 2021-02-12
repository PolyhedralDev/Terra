package com.dfsek.terra.api.math.noise.samplers;

import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.parsii.defined.UserDefinedFunction;
import com.dfsek.terra.api.math.parsii.noise.NoiseFunction2;
import com.dfsek.terra.api.math.parsii.noise.NoiseFunction3;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;

import java.util.Map;

/**
 * Sampler implementation using parsii expression
 */
public class ExpressionSampler implements NoiseSampler {
    private final Expression expression;

    public ExpressionSampler(String equation, Scope parent, long seed, Map<String, NoiseSeeded> functions, Map<String, FunctionTemplate> definedFunctions) throws ParseException {
        Parser parser = new Parser();
        Scope scope = new Scope().withParent(parent);

        scope.addInvocationVariable("x");
        scope.addInvocationVariable("y");
        scope.addInvocationVariable("z");

        functions.forEach((id, noise) -> {
            switch(noise.getDimensions()) {
                case 2:
                    parser.registerFunction(id, new NoiseFunction2(seed, noise));
                    break;
                case 3:
                    parser.registerFunction(id, new NoiseFunction3(seed, noise));
                    break;
            }
        });

        for(Map.Entry<String, FunctionTemplate> entry : definedFunctions.entrySet()) {
            parser.registerFunction(entry.getKey(), UserDefinedFunction.newInstance(entry.getValue(), parser, parent));
        }

        this.expression = parser.parse(equation, scope);
    }

    @Override
    public double getNoise(double x, double y) {
        return getNoise(x, 0, y);
    }

    @Override
    public double getNoise(double x, double y, double z) {
        return expression.evaluate(x, y, z);
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y) {
        return getNoise(x, y);
    }

    @Override
    public double getNoiseSeeded(int seed, double x, double y, double z) {
        return getNoise(x, y, z);
    }
}
