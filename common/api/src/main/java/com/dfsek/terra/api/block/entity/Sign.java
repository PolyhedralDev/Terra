/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block.entity;

import org.checkerframework.checker.nullness.qual.NonNull;


public interface Sign extends BlockEntity {
    void setLine(int index, @NonNull String line) throws IndexOutOfBoundsException;
    
    @NonNull String[] getLines();
    
    @NonNull String getLine(int index) throws IndexOutOfBoundsException;
}
