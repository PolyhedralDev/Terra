package com.dfsek.terra.api.math.parsii;

import parsii.eval.Expression;
import parsii.eval.Function;

import java.util.List;

public class BlankFunction implements Function {
    private final int args;

    public BlankFunction(int args) {
        this.args = args;
    }

    @Override
    public int getNumberOfArguments() {
        return args;
    }

    @Override
    public double eval(List<Expression> list) {
        return 0;
    }

    @Override
    public boolean isNaturalFunction() {
        return true;
    }
}
