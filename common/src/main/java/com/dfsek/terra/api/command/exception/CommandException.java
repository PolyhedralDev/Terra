package com.dfsek.terra.api.command.exception;

public abstract class CommandException extends Exception {
    private static final long serialVersionUID = -2955328495045879822L;

    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
