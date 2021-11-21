/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.profiler;

public class ProfileFrame implements AutoCloseable {
    private final Runnable action;
    
    public ProfileFrame(Runnable action) {
        this.action = action;
    }
    
    @Override
    public void close() {
        action.run();
    }
}
