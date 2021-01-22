package com.dfsek.terra.api.util.mutable;

public class MutableInteger extends Number implements MutablePrimitive<Integer> {
    private int value;

    public MutableInteger() {

    }

    public MutableInteger(int init) {
        this.value = init;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public Integer get() {
        return value;
    }

    @Override
    public void set(Integer value) {
        this.value = value;
    }

    public void add() {
        add(1);
    }

    public void add(int add) {
        this.value += add;
    }
}
