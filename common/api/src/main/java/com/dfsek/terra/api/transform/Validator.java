package com.dfsek.terra.api.transform;


import java.util.Objects;

import com.dfsek.terra.api.transform.exception.TransformException;


public interface Validator<T> {
    static <T> Validator<T> notNull() {
        return Objects::nonNull;
    }
    
    boolean validate(T value) throws TransformException;
}
