/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.tokenizer;

public class SourcePosition {
    private final int line;
    private final int index;
    
    public SourcePosition(int line, int index) {
        this.line = line;
        this.index = index;
    }
    
    @Override
    public String toString() {
        return (line + 1) + ":" + index;
    }
}
