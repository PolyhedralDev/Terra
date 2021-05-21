package com.dfsek.terra.api.math;

import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Random;

public class Range implements Iterable<Integer> {
    private int min;
    private int max;

    public Range(int min, int max) {
        if(min > max) throw new IllegalArgumentException("Minimum must not be grater than maximum!");
        this.max = max;
        this.min = min;
    }

    public boolean isInRange(int test) {
        return test >= min && test < max;
    }

    public int getMax() {
        return max;
    }

    public Range setMax(int max) {
        this.max = max;
        return this;
    }

    public int getMin() {
        return min;
    }

    public Range setMin(int min) {
        this.min = min;
        return this;
    }

    public int getRange() {
        return max - min;
    }

    public Range multiply(int mult) {
        min *= mult;
        max *= mult;
        return this;
    }

    public Range reflect(int pt) {
        return new Range(2 * pt - this.getMax(), 2 * pt - this.getMin());
    }

    public int get(Random r) {
        return r.nextInt((max - min) + 1) + min;
    }

    public Range intersects(com.dfsek.terra.api.math.Range other) {
        try {
            return new Range(FastMath.max(this.getMin(), other.getMin()), FastMath.min(this.getMax(), other.getMax()));
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    public Range add(int add) {
        this.min += add;
        this.max += add;
        return this;
    }

    public Range sub(int sub) {
        this.min -= sub;
        this.max -= sub;
        return this;
    }

    @Override
    public String toString() {
        return "Min: " + getMin() + ", Max:" + getMax();
    }

    @Override
    public int hashCode() {
        return min * 31 + max;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof com.dfsek.terra.api.math.Range)) return false;
        Range other = (com.dfsek.terra.api.math.Range) obj;
        return other.getMin() == this.getMin() && other.getMax() == this.getMax();
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new RangeIterator(this);
    }

    private static class RangeIterator implements Iterator<Integer> {
        private final Range m;
        private Integer current;

        public RangeIterator(Range m) {
            this.m = m;
            current = m.getMin();
        }

        @Override
        public boolean hasNext() {
            return current < m.getMax();
        }

        @Override
        public Integer next() {
            current++;
            return current - 1;
        }
    }
}

