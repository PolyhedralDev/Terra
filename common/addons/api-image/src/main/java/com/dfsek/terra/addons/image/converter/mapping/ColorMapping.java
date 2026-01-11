package com.dfsek.terra.addons.image.converter.mapping;

import java.util.Map;
import java.util.function.Supplier;


public interface ColorMapping<T> extends Supplier<Map<Integer, T>> {
}
