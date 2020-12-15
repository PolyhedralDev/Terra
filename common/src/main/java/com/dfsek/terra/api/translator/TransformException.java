package com.dfsek.terra.api.translator;

public class TransformException extends Exception {
    public TransformException() {
        super();
    }

    public TransformException(String message) {
        super(message);
    }

    public TransformException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransformException(Throwable cause) {
        super(cause);
    }
}
