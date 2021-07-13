package com.dfsek.terra.api.transform;


import com.dfsek.terra.api.transform.exception.TransformException;

import java.util.Objects;

public interface Validator<T> {
    static <T> Validator<T> notNull() {
        return Objects::nonNull;
    }

    boolean validate(T value) throws TransformException;
}
