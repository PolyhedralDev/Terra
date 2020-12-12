package com.dfsek.terra.api.gaea.math;

import com.dfsek.terra.api.gaea.biome.NormalizationUtil;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unchecked")
public class ProbabilityCollection<E> {
    private final Set<Object> cont = new HashSet<>();
    private Object[] array = new Object[0];
    private int size;

    public com.dfsek.terra.api.gaea.math.ProbabilityCollection<E> add(E item, int probability) {
        if(!cont.contains(item)) size++;
        cont.add(item);
        int oldLength = array.length;
        Object[] newArray = new Object[array.length + probability];
        System.arraycopy(array, 0, newArray, 0, array.length); // Expand array.
        array = newArray;
        for(int i = oldLength; i < array.length; i++) array[i] = item;
        return this;
    }

    public E get() {
        if(array.length == 0) return null;
        return (E) array[ThreadLocalRandom.current().nextInt(array.length)];
    }

    public E get(Random r) {
        if(array.length == 0) return null;
        return (E) array[r.nextInt(array.length)];
    }

    public E get(FastNoiseLite n, double x, double z) {
        if(array.length == 0) return null;
        return (E) array[NormalizationUtil.normalize(n.getNoise(x, z), array.length, 1)];
    }

    public int getTotalProbability() {
        return array.length;
    }

    public int size() {
        return size;
    }
}
