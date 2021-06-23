package com.dfsek.terra.api.command.exception;

public class InvalidArgumentsException extends CommandException {
    private static final long serialVersionUID = 7563619667472569824L;

    public InvalidArgumentsException(String message) {
        super(message);
    }

    public InvalidArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }
}
