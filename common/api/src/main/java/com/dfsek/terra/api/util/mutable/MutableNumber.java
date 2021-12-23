/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.mutable;

import java.io.Serial;


public abstract class MutableNumber<T extends Number> extends Number implements MutablePrimitive<T> {
    
    @Serial
    private static final long serialVersionUID = 8619508342781664393L;
    protected T value;
    
    public MutableNumber(T value) {
        this.value = value;
    }
    
    public abstract void increment();
    
    public abstract void decrement();
    
    public abstract void add(T add);
    
    public abstract void multiply(T mul);
    
    public abstract void subtract(T sub);
    
    public abstract void divide(T divide);
    
    @Override
    public T get() {
        return value;
    }
    
    @Override
    public void set(T value) {
        this.value = value;
    }
    
    @Override
    public int intValue() {
        return value.intValue();
    }
    
    @Override
    public long longValue() {
        return value.longValue();
    }
    
    @Override
    public float floatValue() {
        return value.floatValue();
    }
    
    @Override
    public double doubleValue() {
        return value.doubleValue();
    }
}
