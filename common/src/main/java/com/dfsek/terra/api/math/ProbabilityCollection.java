package com.dfsek.terra.api.math;

import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import net.jafama.FastMath;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ProbabilityCollection<E> {
    private final Set<Object> cont = new HashSet<>();
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

    private static double getNoise(double x, double y, double z, NoiseSampler sampler) {
        double n = sampler.getNoise(x, y, z);
        return FastMath.min(FastMath.max(n, -1), 1);
    }

    private static double getNoise(double x, double z, NoiseSampler sampler) {
        double n = sampler.getNoise(x, z);
        return FastMath.min(FastMath.max(n, -1), 1);
    }

    public E get(NoiseSampler n, double x, double y, double z) {
        if(array.length == 0) return null;
        return (E) array[FastMath.min(FastMath.floorToInt(((getNoise(x, y, z, n) + 1D) / 2D) * array.length), array.length - 1)];
    }

    public E get(NoiseSampler n, double x, double z) {
        if(array.length == 0) return null;
        return (E) array[FastMath.min(FastMath.floorToInt(((getNoise(x, z, n) + 1D) / 2D) * array.length), array.length - 1)];
    }

    public int getTotalProbability() {
        return array.length;
    }

    public int size() {
        return size;
    }
}
