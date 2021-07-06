package com.dfsek.terra.api.util.collection;

import com.dfsek.terra.api.noise.NoiseSampler;

import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public interface ProbabilityCollection<E> extends Collection<E> {
    ProbabilityCollection<E> add(E item, int probability);

    E get(Random r);

    E get(NoiseSampler n, double x, double y, double z);

    E get(NoiseSampler n, double x, double z);

    <T> ProbabilityCollection<T> map(Function<E, T> mapper, boolean carryNull);

    int getTotalProbability();

    int getProbability(E item);

    Set<E> getContents();
}
