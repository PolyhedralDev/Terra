/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.manifest.impl.exception;

import java.io.Serial;


public class ManifestException extends AddonException {
    @Serial
    private static final long serialVersionUID = -2458077663176544645L;
    
    public ManifestException(String message) {
        super(message);
    }
    
    public ManifestException(String message, Throwable cause) {
        super(message, cause);
    }
}
