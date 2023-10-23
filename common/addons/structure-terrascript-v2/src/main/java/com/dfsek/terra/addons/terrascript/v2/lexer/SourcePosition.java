/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.v2.lexer;

import java.util.Objects;


public record SourcePosition(int line, int column) {
    
    @Override
    public String toString() {
        return "line " + line + ", column " + column;
    }
    
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        SourcePosition that = (SourcePosition) o;
        return line == that.line && column == that.column;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(line, column);
    }
}
