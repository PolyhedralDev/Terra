package com.dfsek.terra.registry.exception;

public class DuplicateEntryException extends Exception {
    private static final long serialVersionUID = -7199021672428288780L;

    public DuplicateEntryException(String message) {
        super(message);
    }

    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}
