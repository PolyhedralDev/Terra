package com.dfsek.terra.api.structures.tokenizer;

public class Position {
    private final int line;
    private final int index;

    public Position(int line, int index) {
        this.line = line;
        this.index = index;
    }
}
