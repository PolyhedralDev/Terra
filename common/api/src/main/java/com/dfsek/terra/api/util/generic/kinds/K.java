package com.dfsek.terra.api.util.generic.kinds;

/**
 * Kind of the type T<U>
 */
public interface K<T, U> {
    @SuppressWarnings("unchecked")
    default T self() {
        return (T) this;
    }
}
