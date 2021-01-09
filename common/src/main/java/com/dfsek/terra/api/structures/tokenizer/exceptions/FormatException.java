package com.dfsek.terra.api.structures.tokenizer.exceptions;

import com.dfsek.terra.api.structures.tokenizer.Position;

public class FormatException extends TokenizerException {

    public FormatException(String message, Position position) {
        super(message, position);
    }

    public FormatException(String message, Position position, Throwable cause) {
        super(message, position, cause);
    }
}
