package com.dfsek.terra.api.structures.tokenizer.exceptions;

public abstract class TokenizerException extends Exception {
    public TokenizerException(String s) {
        super(s);
    }

    public TokenizerException() {
        super();
    }

    public TokenizerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenizerException(Throwable cause) {
        super(cause);
    }
}
