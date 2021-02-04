package com.dfsek.terra.api.math.parsii.defined;

import com.dfsek.paralithic.Expression;
import com.dfsek.paralithic.function.dynamic.DynamicFunction;


public class UserDefinedFunction implements DynamicFunction {
    private final Expression expression;
    private final int args;

    public UserDefinedFunction(Expression expression, int args) {
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
}
