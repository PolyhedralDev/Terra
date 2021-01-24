package com.dfsek.terra.api.math.noise.samplers;

import com.dfsek.terra.api.math.noise.NoiseFunction2;
import com.dfsek.terra.api.math.noise.NoiseFunction3;
import com.dfsek.terra.api.math.parsii.RandomFunction;
import com.dfsek.terra.generation.config.NoiseBuilder;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.Map;

/**
 * Sampler implementation using parsii expression
 */
public class ExpressionSampler implements NoiseSampler {
    private final Expression expression;
    private final Variable x;
    private final Variable y;
    private final Variable z;

    public ExpressionSampler(String equation, Scope parent, long seed, Map<String, NoiseBuilder> functions) throws ParseException {
        Parser parser = new Parser();
        Scope scope = new Scope().withParent(parent);

        this.x = scope.create("x");
        this.y = scope.create("y");
        this.z = scope.create("z");

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

        parser.registerFunction("rand", new RandomFunction());

        this.expression = parser.parse(equation, scope);
    }

    @Override
    public synchronized double getNoise(double x, double y) {
        return getNoise(x, 0, y);
    }

    @Override
    public synchronized double getNoise(double x, double y, double z) {
        this.x.setValue(x);
        this.y.setValue(y);
        this.z.setValue(z);
        return expression.evaluate();
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
