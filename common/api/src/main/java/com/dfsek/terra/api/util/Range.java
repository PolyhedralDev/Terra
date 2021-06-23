package com.dfsek.terra.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Random;

public interface Range extends Iterable<Integer> {
    boolean isInRange(int test);

    int getMax();

    ConstantRange setMax(int max);

    int getMin();

    ConstantRange setMin(int min);

    int getRange();

    ConstantRange multiply(int mult);

    ConstantRange reflect(int pt);

    int get(Random r);

    ConstantRange intersects(ConstantRange other);

    ConstantRange add(int add);

    ConstantRange sub(int sub);

    @NotNull
    @Override
    Iterator<Integer> iterator();
}
