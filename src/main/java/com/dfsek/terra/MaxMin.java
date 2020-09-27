package com.dfsek.terra;

import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Random;

public class MaxMin implements Iterable<Integer> {
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

    @Override
    public String toString() {
        return  "Min: " + getMin() + " Max:" + getMax();
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new MaxMinIterator(this);
    }
    private static class MaxMinIterator implements Iterator<Integer> {
        private Integer current;
        private final MaxMin m;

        public MaxMinIterator(MaxMin m) {
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
