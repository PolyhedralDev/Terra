package com.dfsek.terra.addons.terrascript.tokenizer.exceptions;

import com.dfsek.terra.addons.terrascript.api.Position;

public class FormatException extends TokenizerException {

    private static final long serialVersionUID = -791308012940744455L;

    public FormatException(String message, Position position) {
        super(message, position);
    }

    public FormatException(String message, Position position, Throwable cause) {
        super(message, position, cause);
    }
}
