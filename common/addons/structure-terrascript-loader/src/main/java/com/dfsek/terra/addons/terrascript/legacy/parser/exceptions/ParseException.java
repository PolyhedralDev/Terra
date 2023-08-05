/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.exceptions;

import java.io.Serial;

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class ParseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6744390543046766386L;
    private final SourcePosition position;
    
    public ParseException(String message, SourcePosition position) {
        super(message);
        this.position = position;
    }
    
    public ParseException(String message, SourcePosition position, Throwable cause) {
        super(message, cause);
        this.position = position;
    }
    
    @Override
    public String getMessage() {
        return "Error at " + position + ": " + super.getMessage();
    }
    
    public SourcePosition getPosition() {
        return position;
    }
}
