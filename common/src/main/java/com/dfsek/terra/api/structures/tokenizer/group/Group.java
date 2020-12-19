package com.dfsek.terra.api.structures.tokenizer.group;

public interface Group {
    char getBegin();

    char getEnd();

    boolean ignoreInsideSyntax();
}
