package com.dfsek.terra.api.structures.tokenizer.exceptions;

public class FormatException extends TokenizerException {

    public FormatException(String s) {
        super(s);
    }

    public FormatException() {
        super();
    }

    public FormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormatException(Throwable cause) {
        super(cause);
    }
}
