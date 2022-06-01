/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.mutable;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;


public class MutableDouble extends MutableNumber<Double> {
    @Serial
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
    
    @Override
    public int compareTo(@NotNull Double o) {
        return Double.compare(value, o);
    }
}
