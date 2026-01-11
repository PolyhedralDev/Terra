package com.dfsek.terra.api.error;

public record InvalidBlockStateError(Exception exception) implements Invalid {
    @Override
    public String message() {
        return exception.getMessage();
    }
}
