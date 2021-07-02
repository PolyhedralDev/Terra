package com.dfsek.terra.addons.structure.structures.tokenizer.exceptions;

import com.dfsek.terra.addons.structure.structures.parser.exceptions.ParseException;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

public abstract class TokenizerException extends ParseException {

    private static final long serialVersionUID = 2792384010083575420L;

    public TokenizerException(String message, Position position) {
        super(message, position);
    }

    public TokenizerException(String message, Position position, Throwable cause) {
        super(message, position, cause);
    }
}
