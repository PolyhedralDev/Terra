package com.dfsek.terra.api.transform;

public class TransformException extends Exception {
    private static final long serialVersionUID = -6661338369581162084L;

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
