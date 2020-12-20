package com.dfsek.terra.api.structures.parser.lang.operations;

public interface BinaryOperation<T> {
    T apply(T left, T right);
}
