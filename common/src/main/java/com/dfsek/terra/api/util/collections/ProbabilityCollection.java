package com.dfsek.terra.api.util.collections;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ProbabilityCollection<E> implements Collection<E> {
    private final Set<E> cont = new HashSet<>();
    private Object[] array = new Object[0];
    private int size;

    public ProbabilityCollection<E> add(E item, int probability) {
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
        return cont.contains(o);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return cont.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return cont.toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return cont.toArray(a);
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
        return cont.containsAll(c);
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
