/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Random;


public interface Range extends Iterable<Integer> {
    Range multiply(int mult);
    
    Range reflect(int pt);
    
    int get(Random r);
    
    Range intersects(Range other);
    
    Range add(int add);
    
    Range sub(int sub);
    
    @NotNull
    @Override
    Iterator<Integer> iterator();
    
    boolean isInRange(int test);
    
    int getMax();
    
    Range setMax(int max);
    
    int getMin();
    
    Range setMin(int min);
    
    int getRange();
}
