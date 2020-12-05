package com.dfsek.terra.config.exception;

import com.dfsek.tectonic.exception.ConfigException;

public class FileMissingException extends ConfigException {
    private static final long serialVersionUID = 4489395640246760802L;

    public FileMissingException(String message) {
        super(message);
    }

    public FileMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
