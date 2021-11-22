/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang;

public interface Returnable<T> extends Item<T> {
    ReturnType returnType();
    
    enum ReturnType {
        NUMBER(true),
        STRING(true),
        BOOLEAN(false),
        VOID(false),
        OBJECT(false);
        
        private final boolean comparable;
        
        ReturnType(boolean comparable) {
            this.comparable = comparable;
        }
        
        public boolean isComparable() {
            return comparable;
        }
    }
}
