package com.dfsek.terra.addons.loading;

public class AddonLoadException extends Exception {
    public AddonLoadException(String message) {
        super(message);
    }

    public AddonLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
