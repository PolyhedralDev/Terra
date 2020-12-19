package com.dfsek.terra.api.structures.tokenizer.group;

public class LineComment implements Group {
    @Override
    public char getBegin() {
        return '#';
    }

    @Override
    public char getEnd() {
        return '\n';
    }

    @Override
    public boolean ignoreInsideSyntax() {
        return true;
    }
}
