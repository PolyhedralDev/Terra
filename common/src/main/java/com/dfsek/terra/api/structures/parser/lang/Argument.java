package com.dfsek.terra.api.structures.parser.lang;

public interface Argument<T> {
    T parse(String input);
}
