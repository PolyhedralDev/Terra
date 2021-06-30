package com.dfsek.terra.addon.exception;

public class DependencyMissingException extends AddonLoadException {
    private static final long serialVersionUID = -8419489102208521583L;

    public DependencyMissingException(String message) {
        super(message);
    }

    public DependencyMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
