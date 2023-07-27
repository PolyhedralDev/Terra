/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.lexer.exceptions;

import java.io.Serial;

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;


public abstract class TokenizerException extends ParseException {
    
    @Serial
    private static final long serialVersionUID = 2792384010083575420L;
    
    public TokenizerException(String message, SourcePosition position) {
        super(message, position);
    }
    
    public TokenizerException(String message, SourcePosition position, Throwable cause) {
        super(message, position, cause);
    }
}
