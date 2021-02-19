package com.dfsek.terra.api.math.paralithic.defined;

import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.eval.parser.Parser;
import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.paralithic.functions.dynamic.DynamicFunction;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;


public class UserDefinedFunction implements DynamicFunction {
    private final Expression expression;
    private final int args;

    protected UserDefinedFunction(Expression expression, int args) {
        this.expression = expression;
        this.args = args;
    }


    @Override
    public double eval(double... args) {
        return expression.evaluate(args);
    }

    @Override
    public boolean isStateless() {
        return true;
    }

    @Override
    public int getArgNumber() {
        return args;
    }

    public static UserDefinedFunction newInstance(FunctionTemplate template, Parser parser, Scope parent) throws ParseException {

        Scope functionScope = new Scope().withParent(parent);

        template.getArgs().forEach(functionScope::addInvocationVariable);

        return new UserDefinedFunction(parser.parse(template.getFunction(), functionScope), template.getArgs().size());
    }
}
