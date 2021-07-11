package com.dfsek.terra.api.command.exception;

/**
 * Thrown when command is incorrectly defined.
 */
public class MalformedCommandException extends CommandException {
    private static final long serialVersionUID = -5417760860407895496L;

    public MalformedCommandException(String message) {
        super(message);
    }

    public MalformedCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
