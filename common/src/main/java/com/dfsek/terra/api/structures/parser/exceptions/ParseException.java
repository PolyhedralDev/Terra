package com.dfsek.terra.api.structures.parser.exceptions;

import com.dfsek.terra.api.structures.tokenizer.Position;

public class ParseException extends Exception {
    private final Position position;

    public ParseException(String message, Position position) {
        super(message);
        this.position = position;
    }

    public ParseException(String message, Position position, Throwable cause) {
        super(message, cause);
        this.position = position;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + ": " + position;
    }

    public Position getPosition() {
        return position;
    }
}
