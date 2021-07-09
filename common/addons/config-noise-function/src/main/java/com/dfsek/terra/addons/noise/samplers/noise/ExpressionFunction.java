package com.dfsek.terra.addons.noise.samplers.noise;

import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.paralithic.functions.Function;

import java.util.Map;

/**
 * NoiseSampler implementation using a Paralithic expression.
 */
public class ExpressionFunction extends NoiseFunction {
    private final Expression expression;

    public ExpressionFunction(Map<String, Function> functions, String eq, Map<String, Double> vars) throws ParseException {
        super(0);
        Parser p = new Parser();
        Scope scope = new Scope();

        scope.addInvocationVariable("x");
        scope.addInvocationVariable("y");
        scope.addInvocationVariable("z");

        vars.forEach(scope::create);

        functions.forEach(p::registerFunction);

        expression = p.parse(eq, scope);
        frequency = 1;
    }

    @Override
    public double getNoiseRaw(int seed, double x, double y) {
        return expression.evaluate(x, 0, y);
    }

    @Override
    public double getNoiseRaw(int seed, double x, double y, double z) {
        return expression.evaluate(x, y, z);
    }
}
