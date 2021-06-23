package com.dfsek.terra.profiler.exception;

public class MalformedStackException extends ProfilerException {
    private static final long serialVersionUID = -3009539681021691054L;

    public MalformedStackException(String message) {
        super(message);
    }

    public MalformedStackException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedStackException(Throwable cause) {
        super(cause);
    }
}
