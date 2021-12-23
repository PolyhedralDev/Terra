/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.registry.exception;

import java.io.Serial;


/**
 * Thrown when a duplicate entry is found in a registry.
 */
public class DuplicateEntryException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7199021672428288780L;
    
    public DuplicateEntryException(String message) {
        super(message);
    }
    
    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}
