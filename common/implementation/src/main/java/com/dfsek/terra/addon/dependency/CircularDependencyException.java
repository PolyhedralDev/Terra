package com.dfsek.terra.addon.dependency;

public class CircularDependencyException extends DependencyException{
    public CircularDependencyException(String message) {
        super(message);
    }
    
    public CircularDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
