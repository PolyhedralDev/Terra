package com.dfsek.terra.api.transform;


public interface Validator<T> {
    boolean validate(T value) throws TransformException;
}
