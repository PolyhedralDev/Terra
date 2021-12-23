/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.inject.exception;

import java.io.Serial;

import com.dfsek.terra.api.inject.Injector;


/**
 * Thrown when dynamic dependency injection cannot be completed by an {@link Injector}.
 */
public class InjectionException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6929631447064215387L;
    
    public InjectionException(String message) {
        super(message);
    }
    
    public InjectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
