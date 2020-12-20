package com.dfsek.terra.api.structures.parser;

public interface Argument<T> {
    T parse(String input);
}
