package com.dfsek.terra.api.block.entity;

import org.jetbrains.annotations.NotNull;


public interface Sign extends BlockEntity {
    void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException;
    
    @NotNull String[] getLines();
    
    @NotNull String getLine(int index) throws IndexOutOfBoundsException;
}
