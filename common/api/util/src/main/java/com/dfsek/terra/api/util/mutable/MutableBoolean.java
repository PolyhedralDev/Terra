/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.mutable;

import org.jetbrains.annotations.NotNull;


public class MutableBoolean implements MutablePrimitive<Boolean> {
    private boolean value;
    
    public MutableBoolean() {
        this.value = false;
    }
    
    public MutableBoolean(boolean value) {
        this.value = value;
    }
    
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
