package com.dfsek.terra.api.registry.exception;

/**
 * Thrown when a duplicate entry is found in a registry.
 */
public class DuplicateEntryException extends RuntimeException {
    private static final long serialVersionUID = -7199021672428288780L;
    
    public DuplicateEntryException(String message) {
        super(message);
    }
    
    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}
