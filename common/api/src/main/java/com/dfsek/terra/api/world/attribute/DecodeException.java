package com.dfsek.terra.api.world.attribute;

/**
 * The message is shown to users in config warnings.
 */
public class DecodeException extends Exception {
    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
