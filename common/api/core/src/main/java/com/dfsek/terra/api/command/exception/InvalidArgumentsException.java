/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.command.exception;

import java.io.Serial;


public class InvalidArgumentsException extends CommandException {
    @Serial
    private static final long serialVersionUID = 7563619667472569824L;
    
    public InvalidArgumentsException(String message) {
        super(message);
    }
    
    public InvalidArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }
}
