/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block.state.properties.enums;

public enum Half {
    /**
     * The top half of the block, normally with the higher y coordinate.
     */
    TOP,
    /**
     * The bottom half of the block, normally with the lower y coordinate.
     */
    BOTTOM,
    
    /**
     * Some blocks, e.g. slabs, can occupy both halves.
     */
    DOUBLE
}
