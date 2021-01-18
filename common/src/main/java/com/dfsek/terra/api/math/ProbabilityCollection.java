package com.dfsek.terra.api.math;

import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ProbabilityCollection<E> {
    private final Set<E> cont = new HashSet<>();
    private Object[] array = new Object[0];
    private int size;

    public com.dfsek.terra.api.math.ProbabilityCollection<E> add(E item, int probability) {
        if(!cont.contains(item)) size++;
        cont.add(item);
        int oldLength = array.length;
        Object[] newArray = new Object[array.length + probability];
        System.arraycopy(array, 0, newArray, 0, array.length); // Expand array.
        array = newArray;
        for(int i = oldLength; i < array.length; i++) array[i] = item;
        return this;
    }

    public E get(Random r) {
        if(array.length == 0) return null;
        return (E) array[r.nextInt(array.length)];
    }

    public E get(NoiseSampler n, double x, double y, double z) {
        if(array.length == 0) return null;
        return (E) array[MathUtil.normalizeIndex(n.getNoise(x, y, z), array.length)];
    }

    public E get(NoiseSampler n, double x, double z) {
        if(array.length == 0) return null;
        return (E) array[MathUtil.normalizeIndex(n.getNoise(x, z), array.length)];
    }

    public int getTotalProbability() {
        return array.length;
    }

    public int size() {
        return size;
    }

    public Set<E> getContents() {
        return new HashSet<>(cont);
    }

    public static final class Singleton<T> extends ProbabilityCollection<T> {
        private final T single;

        public Singleton(T single) {
            this.single = single;
        }

        @Override
        public ProbabilityCollection<T> add(T item, int probability) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T get(Random r) {
            return single;
        }

        @Override
        public T get(NoiseSampler n, double x, double y, double z) {
            return single;
        }

        @Override
        public T get(NoiseSampler n, double x, double z) {
            return single;
        }

        @Override
        public int getTotalProbability() {
            return 1;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public Set<T> getContents() {
            return Collections.singleton(single);
        }
    }
}
