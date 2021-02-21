package com.dfsek.terra.addons.loading.pre.exception;

import com.dfsek.terra.addons.loading.AddonLoadException;

public class DependencyMissingException extends AddonLoadException {
    private static final long serialVersionUID = -8419489102208521583L;

    public DependencyMissingException(String message) {
        super(message);
    }

    public DependencyMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
