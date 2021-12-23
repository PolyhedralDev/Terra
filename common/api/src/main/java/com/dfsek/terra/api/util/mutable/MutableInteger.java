/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.mutable;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;


public class MutableInteger extends MutableNumber<Integer> {
    @Serial
    private static final long serialVersionUID = -4427935901819632745L;
    
    public MutableInteger(Integer value) {
        super(value);
    }
    
    public void increment() {
        add(1);
    }
    
    public void decrement() {
        subtract(1);
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
    
    @Override
    public int compareTo(@NotNull Integer o) {
        return Integer.compare(value, o);
    }
}
