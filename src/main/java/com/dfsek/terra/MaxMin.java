package com.dfsek.terra;

import java.util.Random;

public class MaxMin {
    private final int min;
    private final int max;
    public MaxMin(int min, int max) {
        this.max = max;
        this.min = min;
    }

    public boolean isInRange(int test) {
        return test >= min && test < max;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }
    public int get(Random r) {
        return r.nextInt((max-min)+1)+min;
    }
}
