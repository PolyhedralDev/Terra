package com.dfsek.terra.addons.image.converter;

public interface ColorConverter<T> {

    T apply(int color);

    Iterable<T> getEntries();
}
