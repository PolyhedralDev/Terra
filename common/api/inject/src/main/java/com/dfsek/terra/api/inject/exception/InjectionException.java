package com.dfsek.terra.api.inject.exception;

import com.dfsek.terra.api.inject.Injector;


/**
 * Thrown when dynamic dependency injection cannot be completed by an {@link Injector}.
 */
public class InjectionException extends Exception {
    private static final long serialVersionUID = -6929631447064215387L;
    
    public InjectionException(String message) {
        super(message);
    }
    
    public InjectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
