package com.dfsek.terra.addon.dependency;

public class DependencyVersionException extends DependencyException{
    public DependencyVersionException(String message) {
        super(message);
    }
    
    public DependencyVersionException(String message, Throwable cause) {
        super(message, cause);
    }
}
