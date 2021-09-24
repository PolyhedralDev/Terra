package com.dfsek.terra.api.command.exception;

public class SwitchFormatException extends CommandException {
    private static final long serialVersionUID = -965858989317844628L;
    
    public SwitchFormatException(String message) {
        super(message);
    }
    
    public SwitchFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
