package com.dfsek.terra.addons.manifest.impl.exception;

public class AddonException extends RuntimeException {
    public AddonException(String message) {
        super(message);
    }
    
    public AddonException(String message, Throwable cause) {
        super(message, cause);
    }
}
