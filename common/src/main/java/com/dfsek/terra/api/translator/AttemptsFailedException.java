package com.dfsek.terra.api.translator;

public class AttemptsFailedException extends RuntimeException {
    public AttemptsFailedException() {
        super();
    }

    public AttemptsFailedException(String message) {
        super(message);
    }

    public AttemptsFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttemptsFailedException(Throwable cause) {
        super(cause);
    }
}
