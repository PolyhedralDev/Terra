package com.dfsek.terra.api.transform;

public class NotNullValidator<T> implements Validator<T> {
    @Override
    public boolean validate(T value) {
        return !(value == null);
    }
}
