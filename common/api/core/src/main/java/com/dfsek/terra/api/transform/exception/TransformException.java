/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.transform.exception;

import java.io.Serial;


public class TransformException extends Exception {
    @Serial
    private static final long serialVersionUID = -6661338369581162084L;
    
    public TransformException() {
        super();
    }
    
    public TransformException(String message) {
        super(message);
    }
    
    public TransformException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TransformException(Throwable cause) {
        super(cause);
    }
}
