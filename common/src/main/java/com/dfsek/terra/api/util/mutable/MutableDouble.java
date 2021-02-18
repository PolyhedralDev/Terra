package com.dfsek.terra.api.util.mutable;

public class MutableDouble extends MutableNumber<Double> {
    private static final long serialVersionUID = -2218110876763640053L;

    public MutableDouble(Double value) {
        super(value);
    }

    @Override
    public void increment() {
        add(1d);
    }

    @Override
    public void decrement() {
        subtract(1d);
    }

    @Override
    public void add(Double add) {
        value += add;
    }

    @Override
    public void multiply(Double mul) {
        value *= mul;
    }

    @Override
    public void subtract(Double sub) {
        value -= sub;
    }

    @Override
    public void divide(Double divide) {
        value /= divide;
    }
}
