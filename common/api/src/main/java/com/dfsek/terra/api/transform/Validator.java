package com.dfsek.terra.api.transform;


import com.dfsek.terra.api.transform.exception.TransformException;

import java.util.Objects;

public interface Validator<T> {
    boolean validate(T value) throws TransformException;

    static <T> Validator<T> notNull() {
        return Objects::nonNull;
    }
}
