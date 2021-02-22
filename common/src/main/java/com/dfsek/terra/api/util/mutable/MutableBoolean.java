package com.dfsek.terra.api.util.mutable;

import org.jetbrains.annotations.NotNull;

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

    @Override
    public int compareTo(@NotNull Boolean o) {
        return Boolean.compare(value, o);
    }
}
