package com.dfsek.terra.api.translator;

/**
 * Interface to transform data from one type to another.
 */
@FunctionalInterface
public interface Transform<F, T> {
    T transform(F input) throws TransformException;
}
