package com.dfsek.terra.addons.manifest.impl.exception;

public class ManifestNotPresentException extends ManifestException {
    public ManifestNotPresentException(String message) {
        super(message);
    }
    
    public ManifestNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }
}
