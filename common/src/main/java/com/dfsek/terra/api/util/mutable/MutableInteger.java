package com.dfsek.terra.api.util.mutable;

public class MutableInteger extends MutableNumber<Integer> {
    public MutableInteger(Integer value) {
        super(value);
    }

    public void increment() {
        add(1);
    }

    public void decrement() {
        add(-1);
    }

    @Override
    public void add(Integer add) {
        value += add;
    }

    @Override
    public void multiply(Integer mul) {
        value *= mul;
    }

    @Override
    public void subtract(Integer sub) {
        value -= sub;
    }

    @Override
    public void divide(Integer divide) {
        value /= divide;
    }

    public void add(int add) {
        value += add;
    }
}
