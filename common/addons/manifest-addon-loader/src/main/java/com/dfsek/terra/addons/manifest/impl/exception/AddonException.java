/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.manifest.impl.exception;

import java.io.Serial;


public class AddonException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4201912399458420090L;
    
    public AddonException(String message) {
        super(message);
    }
    
    public AddonException(String message, Throwable cause) {
        super(message, cause);
    }
}
