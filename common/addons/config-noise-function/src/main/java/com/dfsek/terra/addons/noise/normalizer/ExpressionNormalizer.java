package com.dfsek.terra.addons.noise.normalizer;

import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.paralithic.functions.Function;

import java.util.Map;

import com.dfsek.terra.api.noise.NoiseSampler;


public class ExpressionNormalizer extends Normalizer {

    private final Expression expression;

    public ExpressionNormalizer(NoiseSampler sampler, Map<String, Function> functions, String eq, Map<String, Double> vars)
    throws ParseException {
        super(sampler);
        Parser p = new Parser();
        Scope scope = new Scope();
        scope.addInvocationVariable("in");
        vars.forEach(scope::create);
        functions.forEach(p::registerFunction);
        expression = p.parse(eq, scope);
    }

    @Override
    public double normalize(double in) {
        return expression.evaluate(in);
    }
}
