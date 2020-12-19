package com.dfsek.terra.api.structures.tokenizer.exceptions;

public class EOFException extends TokenizerException {

    public EOFException(String s) {
        super(s);
    }

    public EOFException() {
        super();
    }

    public EOFException(String message, Throwable cause) {
        super(message, cause);
    }

    public EOFException(Throwable cause) {
        super(cause);
    }
}
