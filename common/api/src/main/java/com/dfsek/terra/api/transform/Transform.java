package com.dfsek.terra.api.transform;

import com.dfsek.terra.api.transform.exception.TransformException;

/**
 * Interface to transform data from one type to another.
 */
@FunctionalInterface
public interface Transform<F, T> {
    T transform(F input) throws TransformException;
}
