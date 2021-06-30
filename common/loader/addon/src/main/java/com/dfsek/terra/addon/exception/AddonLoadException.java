package com.dfsek.terra.addon.exception;

public class AddonLoadException extends Exception {
    private static final long serialVersionUID = -4949084729296580176L;

    public AddonLoadException(String message) {
        super(message);
    }

    public AddonLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
