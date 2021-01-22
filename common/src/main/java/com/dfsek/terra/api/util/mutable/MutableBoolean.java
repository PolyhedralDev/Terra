package com.dfsek.terra.api.util.mutable;

public class MutableBoolean implements MutablePrimitive<Boolean> {
    private boolean value;

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }

    public boolean invert() {
        value = !value;
        return value;
    }
}
