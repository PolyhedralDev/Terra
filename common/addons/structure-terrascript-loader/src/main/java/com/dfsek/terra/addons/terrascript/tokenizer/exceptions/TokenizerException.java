/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.tokenizer.exceptions;

import java.io.Serial;

import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public abstract class TokenizerException extends ParseException {
    
    @Serial
    private static final long serialVersionUID = 2792384010083575420L;
    
    public TokenizerException(String message, Position position) {
        super(message, position);
    }
    
    public TokenizerException(String message, Position position, Throwable cause) {
        super(message, position, cause);
    }
}
