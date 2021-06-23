package com.dfsek.terra.api.transform.exception;


import java.util.ArrayList;
import java.util.List;

public class AttemptsFailedException extends RuntimeException {
    private static final long serialVersionUID = -1160459550006067137L;
    private final List<Throwable> causes;

    public AttemptsFailedException(String message, List<Throwable> causes) {
        super(message);
        this.causes = causes;
    }

    public List<Throwable> getCauses() {
        return new ArrayList<>(causes);
    }
}
