package com.dfsek.terra.api.math.noise.normalizer;

import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.parsii.noise.NoiseFunction2;
import com.dfsek.terra.api.math.parsii.noise.NoiseFunction3;

import java.util.Map;

public class ExpressionNormalizer extends Normalizer {
    private final Expression expression;

    public ExpressionNormalizer(NoiseSampler sampler, String eq, Map<String, Double> vars) throws ParseException {
        super(sampler);
        Parser p = new Parser();
        Scope scope = new Scope();
        scope.addInvocationVariable("in");

        vars.forEach(scope::create);

        p.registerFunction("super2", new NoiseFunction2(sampler));
        p.registerFunction("super3", new NoiseFunction3(sampler));
        expression = p.parse(eq);
    }

    @Override
    public double normalize(double in) {
        return expression.evaluate(in);
    }
}
