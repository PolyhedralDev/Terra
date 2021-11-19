/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.command.exception;

import java.io.Serial;


/**
 * Thrown when command is incorrectly defined.
 */
public class MalformedCommandException extends CommandException {
    @Serial
    private static final long serialVersionUID = -5417760860407895496L;
    
    public MalformedCommandException(String message) {
        super(message);
    }
    
    public MalformedCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
