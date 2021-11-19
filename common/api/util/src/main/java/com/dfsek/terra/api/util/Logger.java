/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util;

public interface Logger {
    void info(String message);
    
    void warning(String message);
    
    void severe(String message);
    
    default void stack(Throwable t) {
        t.printStackTrace();
    }
}
