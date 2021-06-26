package com.dfsek.terra.api.block.state;

import org.jetbrains.annotations.NotNull;

public interface Sign extends BlockEntity {
    @NotNull String[] getLines();

    @NotNull String getLine(int index) throws IndexOutOfBoundsException;

    void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException;
}
