package com.dfsek.terra.api.command.exception;

public class ExecutionException extends CommandException {
    private static final long serialVersionUID = -6345523475880607959L;

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
