/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block.entity;

import org.jetbrains.annotations.NotNull;


public interface Sign extends BlockEntity {
    void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException;
    
    @NotNull String[] getLines();
    
    @NotNull String getLine(int index) throws IndexOutOfBoundsException;
}
