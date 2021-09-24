package com.dfsek.terra.api.transform;

public interface Transformer<F, T> {
    /**
     * Translate data from {@code from} type to {@code to} type.
     *
     * @param from Data to translate
     *
     * @return Result
     */
    T translate(F from);
}
