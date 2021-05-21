package com.dfsek.terra.api.math.paralithic;


import com.dfsek.paralithic.functions.dynamic.DynamicFunction;

public class BlankFunction implements DynamicFunction {
    private final int args;

    public BlankFunction(int args) {
        this.args = args;
    }

    @Override
    public int getArgNumber() {
        return args;
    }

    @Override
    public double eval(double... d) {
        return 0;
    }

    @Override
    public boolean isStateless() {
        return true;
    }
}
