package com.dfsek.terra;

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

    public int getMin() {
        return min;
    }

    public int get(Random r) {
        return r.nextInt((max-min)+1)+min;
    }

    public Range intersects(Range other) {
        try {
            return new Range(Math.max(this.getMin(), other.getMin()), Math.min(this.getMax(), other.getMax()));
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
        return  "Min: " + getMin() + ", Max:" + getMax();
    }

    @Override
    public int hashCode() {
        return min * 31 + max;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Range)) return false;
        Range other = (Range) obj;
        return other.getMin() == this.getMin() && other.getMax() == this.getMax();
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new RangeIterator(this);
    }
    private static class RangeIterator implements Iterator<Integer> {
        private Integer current;
        private final Range m;

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
