package com.dfsek.terra.api.math.parsii.defined;

import parsii.eval.Expression;
import parsii.eval.Function;
import parsii.eval.Variable;

import java.util.List;

public class UserDefinedFunction implements Function {
    private final Expression expression;
    private final List<Variable> variables;

    public UserDefinedFunction(Expression expression, List<Variable> variables) {
        this.expression = expression;
        this.variables = variables;
    }

    @Override
    public int getNumberOfArguments() {
        return variables.size();
    }

    @Override
    public synchronized double eval(List<Expression> args) {
        for(int i = 0; i < variables.size(); i++) {
            variables.get(i).setValue(args.get(i).evaluate());
        }
        return expression.evaluate();
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }
}
