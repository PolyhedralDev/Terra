/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.transform.exception;


import java.io.Serial;
import java.util.ArrayList;
import java.util.List;


/**
 * Thrown when all transformation attempts fail
 */
public class AttemptsFailedException extends RuntimeException {
    @Serial
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
