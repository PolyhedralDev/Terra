package com.dfsek.terra.addons.image.converter;

import java.util.function.Function;


public interface ColorConverter<T> extends Function<Integer, T> {
    Iterable<T> getEntries();
}
