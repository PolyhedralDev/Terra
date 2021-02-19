package com.dfsek.terra.api.structures.tokenizer.exceptions;

import com.dfsek.terra.api.structures.tokenizer.Position;

public class EOFException extends TokenizerException {

    private static final long serialVersionUID = 3980047409902809440L;

    public EOFException(String message, Position position) {
        super(message, position);
    }

    public EOFException(String message, Position position, Throwable cause) {
        super(message, position, cause);
    }
}
