/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;


public interface Range extends Iterable<Integer> {
    Range multiply(int mult);
    
    Range reflect(int pt);
    
    int get(RandomGenerator r);
    
    Range intersects(Range other);
    
    Range add(int add);
    
    Range sub(int sub);
    
    @NonNull
    @Override
    Iterator<Integer> iterator();
    
    boolean isInRange(int test);
    
    int getMax();
    
    Range setMax(int max);
    
    int getMin();
    
    Range setMin(int min);
    
    int getRange();
    
    default <T> T ifInRange(int y, T inRange, T notInRange) {
        if(isInRange(y)) return inRange;
        return notInRange;
    }
    
    default <T> T ifInRange(int y, Supplier<T> inRange, Supplier<T> notInRange) {
        if(isInRange(y)) return inRange.get();
        return notInRange.get();
    }
    
    default <T> T ifInRange(int y, Supplier<T> inRange, T notInRange) {
        if(isInRange(y)) return inRange.get();
        return notInRange;
    }
    
    default <T> T ifInRange(int y, T inRange, Supplier<T> notInRange) {
        if(isInRange(y)) return inRange;
        return notInRange.get();
    }
}
