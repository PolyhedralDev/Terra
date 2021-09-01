package com.dfsek.terra.profiler.exception;

public class ProfilerException extends RuntimeException {
    private static final long serialVersionUID = 8206737998791649002L;
    
    public ProfilerException(String message) {
        super(message);
    }
    
    public ProfilerException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ProfilerException(Throwable cause) {
        super(cause);
    }
}
