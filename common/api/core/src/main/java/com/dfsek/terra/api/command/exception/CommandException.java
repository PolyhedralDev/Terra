/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.command.exception;

import java.io.Serial;


public abstract class CommandException extends Exception {
    @Serial
    private static final long serialVersionUID = -2955328495045879822L;
    
    public CommandException(String message) {
        super(message);
    }
    
    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
