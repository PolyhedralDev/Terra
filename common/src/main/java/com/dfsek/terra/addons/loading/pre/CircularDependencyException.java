package com.dfsek.terra.addons.loading.pre;

import com.dfsek.terra.addons.loading.AddonLoadException;

public class CircularDependencyException extends AddonLoadException {
    private static final long serialVersionUID = 7398510879124125121L;

    public CircularDependencyException(String message) {
        super(message);
    }

    public CircularDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
