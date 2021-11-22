/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.manifest.impl.exception;

import java.io.Serial;


public class ManifestNotPresentException extends ManifestException {
    @Serial
    private static final long serialVersionUID = -2116663180747013810L;
    
    public ManifestNotPresentException(String message) {
        super(message);
    }
    
    public ManifestNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }
}
