package com.dfsek.terra.api.math.noise.samplers;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.parsii.defined.UserDefinedFunction;
import com.dfsek.terra.api.math.parsii.noise.NoiseFunction2;
import com.dfsek.terra.api.math.parsii.noise.NoiseFunction3;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Sampler implementation using parsii expression
 */
public class ExpressionSampler implements NoiseSampler {
    private final Expression expression;
    private final Variable x;
    private final Variable y;
    private final Variable z;

    public ExpressionSampler(String equation, Scope parent, long seed, Map<String, NoiseSeeded> functions, Map<String, FunctionTemplate> definedFunctions) throws ParseException {
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

        for(Map.Entry<String, FunctionTemplate> entry : definedFunctions.entrySet()) {
            String id = entry.getKey();
            FunctionTemplate fun = entry.getValue();

            Scope functionScope = new Scope().withParent(parent);
            List<Variable> variables = fun.getArgs().stream().map(functionScope::create).collect(Collectors.toList());

            parser.registerFunction(id, new UserDefinedFunction(parser.parse(fun.getFunction(), functionScope), variables));
        }

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
