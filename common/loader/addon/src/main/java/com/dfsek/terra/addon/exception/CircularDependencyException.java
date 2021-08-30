package com.dfsek.terra.addon.exception;

public class CircularDependencyException extends AddonLoadException {
    private static final long serialVersionUID = 7398510879124125121L;
    
    public CircularDependencyException(String message) {
        super(message);
    }
    
    public CircularDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
