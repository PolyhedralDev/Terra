package com.dfsek.terra.api.transform;

import com.dfsek.terra.api.util.GlueList;

import java.util.List;

public class AttemptsFailedException extends RuntimeException {
    private final List<Throwable> causes;

    public AttemptsFailedException(String message, List<Throwable> causes) {
        super(message);
        this.causes = causes;
    }

    public List<Throwable> getCauses() {
        return new GlueList<>(causes);
    }
}
