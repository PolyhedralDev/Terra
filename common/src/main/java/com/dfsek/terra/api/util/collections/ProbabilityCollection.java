package com.dfsek.terra.api.util.collections;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.util.mutable.MutableInteger;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class ProbabilityCollection<E> implements Collection<E> {
    protected final Map<E, MutableInteger> cont = new HashMap<>();
    private Object[] array = new Object[0];
    private int size;

    public ProbabilityCollection<E> add(E item, int probability) {
        if(!cont.containsKey(item)) size++;
        cont.computeIfAbsent(item, i -> new MutableInteger(0)).increment();
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

    public <T> ProbabilityCollection<T> map(Function<E, T> mapper, boolean carryNull) {
        ProbabilityCollection<T> newCollection = new ProbabilityCollection<>();
        newCollection.array = new Object[array.length];

        for(int i = 0; i < array.length; i++) {
            if(carryNull && array[i] == null) continue;
            newCollection.array[i] = mapper.apply((E) array[i]);
        }
        return newCollection;
    }

    public int getTotalProbability() {
        return array.length;
    }

    public int getProbability(E item) {
        MutableInteger integer = cont.get(item);
        return integer == null ? 0 : integer.get();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");

        cont.forEach((item, prob) -> builder.append(item).append(": ").append(prob).append(", "));

        return builder.append("]").toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return array.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        return cont.containsKey(o);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return cont.keySet().iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return cont.keySet().toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return cont.keySet().toArray(a);
    }

    /**
     * Adds an item with probability of 1.
     */
    @Override
    public boolean add(E e) {
        add(e, 1);
        return true; // Since this adds the item with a probability, the collection will always have changed.
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Cannot remove item from ProbabilityCollection!");
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return cont.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Cannot remove item from ProbabilityCollection!");
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Cannot remove item from ProbabilityCollection!");
    }

    @Override
    public void clear() {
        cont.clear();
        array = new Object[0];
    }

    public Set<E> getContents() {
        return new HashSet<>(cont.keySet());
    }

    public static final class Singleton<T> extends ProbabilityCollection<T> {
        private final T single;

        public Singleton(T single) {
            this.single = single;
            cont.put(single, new MutableInteger(1));
        }

        @Override
        public ProbabilityCollection<T> add(T item, int probability) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T1> ProbabilityCollection<T1> map(Function<T, T1> mapper, boolean carryNull) {
            if(carryNull && single == null) return new Singleton<>(null);
            return new Singleton<>(mapper.apply(single));
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
