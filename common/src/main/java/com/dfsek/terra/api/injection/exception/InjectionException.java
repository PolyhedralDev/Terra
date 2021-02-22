package com.dfsek.terra.api.injection.exception;

public class InjectionException extends Exception {
    private static final long serialVersionUID = -6929631447064215387L;

    public InjectionException(String message) {
        super(message);
    }

    public InjectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
